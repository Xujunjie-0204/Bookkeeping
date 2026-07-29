package com.example.bookkeeping.common.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "统一接口返回结构")
public class ApiResult<T> {

    @Schema(description = "是否成功")
    private boolean success;

    @Schema(description = "业务状态码")
    private int code;

    @Schema(description = "提示信息")
    private String message;

    @Schema(description = "响应数据")
    private T data;

    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<T>(true, 200, "操作成功", data);
    }

    public static ApiResult<Void> success() {
        return new ApiResult<Void>(true, 200, "操作成功", null);
    }

    public static <T> ApiResult<T> fail(int code, String message) {
        return new ApiResult<T>(false, code, message, null);
    }
}
