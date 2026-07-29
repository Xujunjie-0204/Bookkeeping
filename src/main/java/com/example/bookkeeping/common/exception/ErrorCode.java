package com.example.bookkeeping.common.exception;

public enum ErrorCode {
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "登录已失效"),
    FORBIDDEN(403, "权限不足"),
    NOT_FOUND(404, "数据不存在"),
    DUPLICATE_DATA(409, "数据已存在"),
    INSUFFICIENT_STOCK(422, "库存不足"),
    BUSINESS_ERROR(5001, "业务处理失败"),
    SYSTEM_ERROR(500, "系统异常");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
