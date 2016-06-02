package com.springboot.security.dao.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "login_history")
@NamedQuery(name = "LoginHistory.findAll", query = "SELECT a FROM LoginHistory a")
public class LoginHistory implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "LOGIN_HISTORY_ID_GENERATOR", sequenceName = "LOGIN_HISTORY_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LOGIN_HISTORY_ID_GENERATOR")
    private Long id;

    @Column(name = "user_info_id")
    private Long userInfoId;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(name = "logined_at")
    private Timestamp loginedAt;

    public LoginHistory() {}

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id セットする id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return userInfoId
     */
    public Long getUserInfoId() {
        return userInfoId;
    }

    /**
     * @param userInfoId セットする userInfoId
     */
    public void setUserInfoId(Long userInfoId) {
        this.userInfoId = userInfoId;
    }

    /**
     * @return ipAddress
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * @param ipAddress セットする ipAddress
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**
     * @return userAgent
     */
    public String getUserAgent() {
        return userAgent;
    }

    /**
     * @param userAgent セットする userAgent
     */
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    /**
     * @return loginedAt
     */
    public Timestamp getLoginedAt() {
        return loginedAt;
    }

    /**
     * @param loginedAt セットする loginedAt
     */
    public void setLoginedAt(Timestamp loginedAt) {
        this.loginedAt = loginedAt;
    }

}
