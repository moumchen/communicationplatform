package com.jixiata.util;

/**
 * 常量枚举
 */
public enum ConstantEnum {
    SUCCESS(200),FAIL(100),PERMISSION_DENIED(0),CHECK_FAIL(-1),IDENTITY_DENIED(-2);

    ConstantEnum(int statusCode) {
        this.statusCode = statusCode;
    }

    private int statusCode;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
