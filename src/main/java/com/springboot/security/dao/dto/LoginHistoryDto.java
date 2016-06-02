package com.springboot.security.dao.dto;

import java.io.Serializable;
import java.sql.Timestamp;

public class LoginHistoryDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    private String ipAddress;

    private Timestamp loginedAt;

    private String userAgent;

    private Long userInfoId;

    public LoginHistoryDto() {
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Timestamp getLoginedAt() {
        return this.loginedAt;
    }

    public void setLoginedAt(Timestamp loginedAt) {
        this.loginedAt = loginedAt;
    }

    public String getUserAgent() {
        return this.userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public Long getUserInfoId() {
        return this.userInfoId;
    }

    public void setUserInfoId(Long userInfoId) {
        this.userInfoId = userInfoId;
    }

}
