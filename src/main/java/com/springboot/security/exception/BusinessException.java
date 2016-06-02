package com.springboot.security.exception;

public class BusinessException extends Exception {

    private static final long serialVersionUID = 1L;

    public BusinessException(String code, String message) {
        super(message + "(CODE:" + code + ")");
        this.code = code;
    }

    public BusinessException(String code, String message, Throwable cause) {
        super(message + "(CODE:" + code + ")", cause);
        this.code = code;
    }

    private String code;

    public String getCode() {
        return code;
    }
}
