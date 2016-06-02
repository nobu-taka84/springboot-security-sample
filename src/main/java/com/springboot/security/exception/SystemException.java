package com.springboot.security.exception;

public class SystemException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public SystemException(String code, String message) {
        super(message + "(CODE:" + code + ")");
        this.code = code;
    }

    public SystemException(String code, String message, Throwable cause) {
        super(message + "(CODE:" + code + ")", cause);
        this.code = code;
    }

    private String code;

    public String getCode() {
        return code;
    }
}
