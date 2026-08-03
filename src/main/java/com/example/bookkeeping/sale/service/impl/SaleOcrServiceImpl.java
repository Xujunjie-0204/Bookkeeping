package com.example.bookkeeping.sale.service.impl;

import com.example.bookkeeping.common.exception.BusinessException;
import com.example.bookkeeping.common.exception.ErrorCode;
import com.example.bookkeeping.sale.service.SaleOcrService;
import com.example.bookkeeping.sale.vo.SaleOcrVO;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SaleOcrServiceImpl implements SaleOcrService {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final List<String> COMMON_TESSDATA_PATHS = Arrays.asList(
            "C:\\Program Files\\Tesseract-OCR\\tessdata",
            "C:\\Program Files (x86)\\Tesseract-OCR\\tessdata"
    );

    private final String tessdataPath;
    private final String language;

    public SaleOcrServiceImpl(@Value("${bookkeeping.ocr.tessdata-path:}") String tessdataPath,
                              @Value("${bookkeeping.ocr.language:chi_sim+eng}") String language) {
        this.tessdataPath = tessdataPath;
        this.language = language;
    }

    @Override
    public SaleOcrVO recognize(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "请上传销售订单截图");
        }
        String contentType = file.getContentType();
        if (contentType == null || (!contentType.startsWith("image/") && !"application/pdf".equals(contentType))) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "仅支持 PNG、JPEG、PDF 文件");
        }

        File tempFile = null;
        try {
            tempFile = createTempFile(file);
            String text = recognizeText(tempFile);
            SaleOcrVO vo = parse(text);
            vo.setRawText(text);
            return vo;
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "OCR 文件读取失败");
        } finally {
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

    private File createTempFile(MultipartFile file) throws IOException {
        String suffix = ".png";
        String originalName = file.getOriginalFilename();
        if (StringUtils.hasText(originalName) && originalName.lastIndexOf('.') >= 0) {
            suffix = originalName.substring(originalName.lastIndexOf('.'));
        }
        File tempFile = Files.createTempFile("sale-ocr-", suffix).toFile();
        file.transferTo(tempFile);
        return tempFile;
    }

    private String recognizeText(File file) {
        ITesseract tesseract = new Tesseract();
        tesseract.setLanguage(language);
        String datapath = resolveTessdataPath();
        tesseract.setDatapath(datapath);
        validateLanguageFiles(datapath);
        try {
            return tesseract.doOCR(file);
        } catch (TesseractException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "OCR 识别失败，请检查 Tesseract 和语言包");
        } catch (Error e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "OCR 底层组件调用失败，请检查 Tesseract 安装目录和 tessdata 路径");
        }
    }

    private SaleOcrVO parse(String text) {
        String normalized = text == null ? "" : text.replace("\r", "\n").replaceAll("[ \\t]+", " ");
        String compact = normalized.replaceAll("\\s+", "");
        List<String> lines = splitLines(normalized);

        SaleOcrVO vo = new SaleOcrVO();
        vo.setPlatform(inferPlatform(normalized));
        vo.setPlatformOrderNo(firstMatch(normalized,
                "订单编号\\s*([0-9A-Za-z]{8,})",
                "订单号\\s*([0-9A-Za-z]{8,})"));
        if (!StringUtils.hasText(vo.getPlatformOrderNo())) {
            vo.setPlatformOrderNo(firstMatch(compact, "订单编号([0-9A-Za-z]{8,})", "订单号([0-9A-Za-z]{8,})"));
        }
        vo.setBusinessDate(normalizeDate(firstMatch(normalized,
                "发货时间\\s*(\\d{4}[-/]\\d{1,2}[-/]\\d{1,2})",
                "付款时间\\s*(\\d{4}[-/]\\d{1,2}[-/]\\d{1,2})",
                "下单时间\\s*(\\d{4}[-/]\\d{1,2}[-/]\\d{1,2})",
                "(\\d{4}[-/]\\d{1,2}[-/]\\d{1,2})\\s+\\d{1,2}:\\d{2}:\\d{2}")));
        vo.setSaleAmount(findAmount(normalized, compact));
        vo.setExpressCompany(firstMatch(normalized, "(京东快递|顺丰|韵达|中通|圆通|申通)"));
        vo.setExpressNo(findExpressNo(normalized, compact));
        vo.setBuyerPhone(firstMatch(normalized, "(1\\d{2}\\*{2,6}\\d{4}|1\\d{10})"));
        vo.setBuyerName(findBuyerName(lines, vo.getBuyerPhone()));
        vo.setProductTitle(findProductTitle(lines));
        vo.setConditionDesc(firstMatch(normalized, "(成色[:：]?\\s*[^\\n\\r]+)", "([0-9]成新)", "([0-9]{2}新)", "(带盒)"));
        vo.setPaymentStatus(1);
        vo.setShipmentStatus(normalized.contains("已发货") ? 1 : 0);
        return vo;
    }

    private String resolveTessdataPath() {
        if (StringUtils.hasText(tessdataPath)) {
            return tessdataPath;
        }
        for (String path : COMMON_TESSDATA_PATHS) {
            if (new File(path).isDirectory()) {
                return path;
            }
        }
        throw new BusinessException(ErrorCode.SYSTEM_ERROR, "未找到 Tesseract 语言包目录，请配置 TESSDATA_PREFIX");
    }

    private void validateLanguageFiles(String datapath) {
        File dir = new File(datapath);
        if (!dir.isDirectory()) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "Tesseract 语言包目录不存在：" + datapath);
        }
        String[] languages = language.split("\\+");
        List<String> missing = new ArrayList<String>();
        for (String item : languages) {
            String lang = item.trim();
            if (StringUtils.hasText(lang) && !new File(dir, lang + ".traineddata").isFile()) {
                missing.add(lang + ".traineddata");
            }
        }
        if (!missing.isEmpty()) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "Tesseract 缺少语言包：" + String.join("、", missing));
        }
    }

    private List<String> splitLines(String text) {
        List<String> list = new ArrayList<String>();
        String[] lines = text.split("\\n");
        for (String line : lines) {
            String value = line.trim();
            if (StringUtils.hasText(value)) {
                list.add(value);
            }
        }
        return list;
    }

    private String inferPlatform(String text) {
        if (text.contains("闲鱼") || text.contains("无忧卖")) {
            return "闲鱼";
        }
        if (text.contains("转转")) {
            return "转转";
        }
        if (text.contains("淘宝") || text.contains("支付宝")) {
            return "淘宝";
        }
        if (text.contains("京东")) {
            return "京东";
        }
        return "闲鱼";
    }

    private BigDecimal findAmount(String text, String compact) {
        String value = firstMatch(text,
                "成交价[^\\d￥¥]*(?:￥|¥)?\\s*(\\d+(?:\\.\\d+)?)",
                "销售金额[^\\d￥¥]*(?:￥|¥)?\\s*(\\d+(?:\\.\\d+)?)",
                "(?:￥|¥)\\s*(\\d+(?:\\.\\d+)?)");
        if (!StringUtils.hasText(value)) {
            value = firstMatch(compact, "成交价(?:￥|¥)?(\\d+(?:\\.\\d+)?)", "(?:￥|¥)(\\d+(?:\\.\\d+)?)");
        }
        return StringUtils.hasText(value) ? new BigDecimal(value) : BigDecimal.ZERO;
    }

    private String findExpressNo(String text, String compact) {
        String value = firstMatch(text,
                "(?:京东快递|顺丰|韵达|中通|圆通|申通)\\s*([A-Za-z]{1,4}\\d{8,})",
                "(JD\\d{8,})",
                "快递单号\\s*([A-Za-z0-9]{8,})");
        if (!StringUtils.hasText(value)) {
            value = firstMatch(compact, "(?:京东快递|顺丰|韵达|中通|圆通|申通)([A-Za-z]{1,4}\\d{8,})", "(JD\\d{8,})");
        }
        return value;
    }

    private String findBuyerName(List<String> lines, String buyerPhone) {
        for (String line : lines) {
            String value = firstMatch(line,
                    "([\\u4e00-\\u9fa5A-Za-z]{2,12})\\s*(?:1\\d{2}\\*{2,6}\\d{4}|1\\d{10})",
                    "([\\u4e00-\\u9fa5A-Za-z]{1,12})(?:先生|女士)");
            if (StringUtils.hasText(value) && !isMetaText(value)) {
                return value;
            }
        }
        if (StringUtils.hasText(buyerPhone)) {
            for (String line : lines) {
                int index = line.indexOf(buyerPhone);
                if (index > 0) {
                    String prefix = line.substring(0, index).replaceAll("[^\\u4e00-\\u9fa5A-Za-z]", "").trim();
                    if (StringUtils.hasText(prefix) && !isMetaText(prefix)) {
                        return prefix.length() > 12 ? prefix.substring(prefix.length() - 12) : prefix;
                    }
                }
            }
        }
        return "";
    }

    private boolean isMetaText(String value) {
        return value.contains("京东") || value.contains("快递") || value.contains("订单")
                || value.contains("成交") || value.contains("地址") || value.contains("时间")
                || value.contains("复制") || value.contains("买家");
    }

    private String findProductTitle(List<String> lines) {
        for (String line : lines) {
            boolean looksProduct = line.contains("小米") || line.contains("摄像") || line.contains("手机")
                    || line.contains("耳机") || line.contains("手柄") || line.contains("内存")
                    || line.contains("路由") || line.contains("主机");
            boolean looksMeta = line.contains("订单") || line.contains("时间") || line.contains("收货") || line.contains("快递");
            if (looksProduct && !looksMeta) {
                return line.replaceAll("￥.*$", "")
                        .replaceAll("48小时发货.*$", "")
                        .replace("无忧卖", "")
                        .trim();
            }
        }
        return "";
    }

    private String firstMatch(String text, String... patterns) {
        for (String pattern : patterns) {
            Matcher matcher = Pattern.compile(pattern).matcher(text);
            if (matcher.find() && matcher.groupCount() >= 1) {
                String value = matcher.group(1);
                if (StringUtils.hasText(value)) {
                    return value.trim();
                }
            }
        }
        return "";
    }

    private String normalizeDate(String value) {
        if (!StringUtils.hasText(value)) {
            return "";
        }
        String[] parts = value.replace("/", "-").split("-");
        if (parts.length != 3) {
            return "";
        }
        return LocalDate.of(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2])).format(DATE_FORMATTER);
    }
}
