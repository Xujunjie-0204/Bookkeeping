package com.example.bookkeeping.purchase.service.impl;

import com.example.bookkeeping.common.exception.BusinessException;
import com.example.bookkeeping.common.exception.ErrorCode;
import com.example.bookkeeping.purchase.service.PurchaseOcrService;
import com.example.bookkeeping.purchase.vo.PurchaseOcrVO;
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
import java.util.Arrays;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PurchaseOcrServiceImpl implements PurchaseOcrService {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final List<String> COMMON_TESSDATA_PATHS = Arrays.asList(
            "C:\\Program Files\\Tesseract-OCR\\tessdata",
            "C:\\Program Files (x86)\\Tesseract-OCR\\tessdata"
    );

    private final String tessdataPath;
    private final String language;

    public PurchaseOcrServiceImpl(@Value("${bookkeeping.ocr.tessdata-path:}") String tessdataPath,
                                  @Value("${bookkeeping.ocr.language:chi_sim+eng}") String language) {
        this.tessdataPath = tessdataPath;
        this.language = language;
    }

    @Override
    public PurchaseOcrVO recognize(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "请上传订单截图");
        }
        String contentType = file.getContentType();
        if (contentType == null || (!contentType.startsWith("image/") && !"application/pdf".equals(contentType))) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "仅支持 PNG、JPEG、PDF 文件");
        }

        File tempFile = null;
        try {
            tempFile = createTempFile(file);
            String text = recognizeText(tempFile);
            PurchaseOcrVO vo = parse(text);
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
        File tempFile = Files.createTempFile("purchase-ocr-", suffix).toFile();
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
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "OCR 识别失败，请确认后端已安装 Tesseract 和中文语言包 chi_sim");
        } catch (Error e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "OCR 底层组件调用失败，请检查 Tesseract 安装目录和 tessdata 语言包路径");
        }
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
        throw new BusinessException(ErrorCode.SYSTEM_ERROR, "未找到 Tesseract 语言包目录，请配置 TESSDATA_PREFIX 为 tessdata 目录");
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
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "Tesseract 缺少语言包：" + String.join("、", missing) + "，目录：" + datapath);
        }
    }

    private PurchaseOcrVO parse(String text) {
        String normalized = text == null ? "" : text.replace("\r", "\n").replaceAll("[ \\t]+", " ");
        String compact = normalized.replaceAll("\\s+", "");
        List<String> lines = splitLines(normalized);

        PurchaseOcrVO vo = new PurchaseOcrVO();
        vo.setPlatform(inferPlatform(normalized));
        vo.setPlatformOrderNo(firstMatch(normalized,
                "订单编号\\s*([0-9A-Za-z]{8,})",
                "订单号\\s*([0-9A-Za-z]{8,})",
                "订单编码\\s*([0-9A-Za-z]{8,})"));
        if (!StringUtils.hasText(vo.getPlatformOrderNo())) {
            vo.setPlatformOrderNo(firstMatch(compact,
                    "订单编号([0-9A-Za-z]{8,})",
                    "订单号([0-9A-Za-z]{8,})"));
        }
        vo.setPurchaseDate(normalizeDateTime(firstMatch(normalized,
                "下单时间\\s*(\\d{4}[-/]\\d{1,2}[-/]\\d{1,2}\\s+\\d{1,2}:\\d{2}:\\d{2})",
                "支付时间\\s*(\\d{4}[-/]\\d{1,2}[-/]\\d{1,2}\\s+\\d{1,2}:\\d{2}:\\d{2})",
                "(\\d{4}[-/]\\d{1,2}[-/]\\d{1,2}\\s+\\d{1,2}:\\d{2}:\\d{2})",
                "下单时间\\s*(\\d{4}[-/]\\d{1,2}[-/]\\d{1,2})",
                "支付时间\\s*(\\d{4}[-/]\\d{1,2}[-/]\\d{1,2})",
                "(\\d{4}[-/]\\d{1,2}[-/]\\d{1,2})\\s+\\d{1,2}:\\d{2}:\\d{2}")));
        vo.setPayAmount(findAmount(normalized, compact));
        vo.setPaymentMethod(inferPaymentMethod(normalized));
        vo.setExpressName(firstMatch(normalized,
                "配送方式\\s*([^\\n]+)",
                "(京东快递|顺丰|韵达|中通|圆通|申通)"));
        vo.setSupplierName(findStoreName(lines, vo.getPlatform()));
        vo.setSellerAccount(vo.getSupplierName());
        vo.setProductTitle(findProductTitle(lines));
        vo.setConditionDesc(firstMatch(normalized,
                "【?\\s*([0-9]成新)\\s*】?",
                "([0-9]{2}新)",
                "(新品\\s*[0-9]{1,3}%)"));
        return vo;
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
        if (text.contains("京东") || text.contains("京东快递") || text.contains("银行卡支付")) {
            return "京东";
        }
        if (text.contains("闲鱼")) {
            return "闲鱼";
        }
        if (text.contains("转转")) {
            return "转转";
        }
        if (text.contains("淘宝")) {
            return "淘宝";
        }
        return "京东";
    }

    private String inferPaymentMethod(String text) {
        if (text.contains("银行卡")) {
            return "银行卡";
        }
        if (text.contains("支付宝")) {
            return "支付宝";
        }
        if (text.contains("微信")) {
            return "微信";
        }
        return "";
    }

    private BigDecimal findAmount(String text, String compact) {
        String value = firstMatch(text,
                "实付款[^\\d￥¥]*(?:￥|¥)?\\s*(\\d+(?:\\.\\d+)?)",
                "合计[^\\d￥¥]*(?:￥|¥)?\\s*(\\d+(?:\\.\\d+)?)",
                "到手[^\\d￥¥]*(?:￥|¥)?\\s*(\\d+(?:\\.\\d+)?)",
                "(?:￥|¥)\\s*(\\d+(?:\\.\\d+)?)");
        if (!StringUtils.hasText(value)) {
            value = firstMatch(compact,
                    "实付款(?:￥|¥)?(\\d+(?:\\.\\d+)?)",
                    "合计(?:￥|¥)?(\\d+(?:\\.\\d+)?)",
                    "到手(?:￥|¥)?(\\d+(?:\\.\\d+)?)");
        }
        return StringUtils.hasText(value) ? new BigDecimal(value) : BigDecimal.ZERO;
    }

    private String findStoreName(List<String> lines, String platform) {
        for (String line : lines) {
            if (line.contains("自营专区") || line.contains("店铺") || line.contains("卖家") || line.contains("商家")) {
                String value = line.replace("自营", "")
                        .replace("拍拍二手", "")
                        .replace(">", "")
                        .replace("›", "")
                        .trim();
                return StringUtils.hasText(value) ? value : line;
            }
        }
        return platform;
    }

    private String findProductTitle(List<String> lines) {
        for (String line : lines) {
            boolean looksProduct = line.contains("成新") || line.contains("小米") || line.contains("摄像")
                    || line.contains("手机") || line.contains("耳机") || line.contains("手柄")
                    || line.contains("内存") || line.contains("路由") || line.contains("主机");
            boolean looksMeta = line.contains("订单") || line.contains("时间") || line.contains("支付") || line.contains("配送");
            if (looksProduct && !looksMeta) {
                return line.replaceAll("数量.*$", "")
                        .replaceAll("到手.*$", "")
                        .replace(">", "")
                        .replace("›", "")
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

    private String normalizeDateTime(String value) {
        if (!StringUtils.hasText(value)) {
            return "";
        }
        String normalized = value.trim().replace("/", "-");
        if (!normalized.contains(":")) {
            return normalizeDate(normalized) + " 00:00:00";
        }
        String[] parts = normalized.split("\\s+");
        if (parts.length < 2) {
            return "";
        }
        String[] dateParts = parts[0].split("-");
        String[] timeParts = parts[1].split(":");
        if (dateParts.length != 3 || timeParts.length < 2) {
            return "";
        }
        int second = timeParts.length >= 3 ? Integer.parseInt(timeParts[2]) : 0;
        return LocalDateTime.of(
                Integer.parseInt(dateParts[0]),
                Integer.parseInt(dateParts[1]),
                Integer.parseInt(dateParts[2]),
                Integer.parseInt(timeParts[0]),
                Integer.parseInt(timeParts[1]),
                second
        ).format(DATE_TIME_FORMATTER);
    }
}
