package com.springboot.security.dao.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "user_privilege_info")
@NamedQuery(name = "UserPrivilegeInfo.findAll", query = "SELECT a FROM UserPrivilegeInfo a")
public class UserPrivilegeInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "USER_PRIVILEGE_INFO_ID_GENERATOR", sequenceName = "USER_PRIVILEGE_INFO_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_PRIVILEGE_INFO_ID_GENERATOR")
    private Long id;

    @Column(name = "user_info_id")
    private Long userInfoId;

    @Column(name = "user_privilege")
    private String userPrivilege;

    @Version
    private Long version;

    public UserPrivilegeInfo() {}

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
     * @param user_info_id セットする userInfoId
     */
    public void setUserInfoId(Long userInfoId) {
        this.userInfoId = userInfoId;
    }

    /**
     * @return userPrivilege
     */
    public String getUserPrivilege() {
        return userPrivilege;
    }

    /**
     * @param userPrivilege セットする userPrivilege
     */
    public void setUserPrivilege(String userPrivilege) {
        this.userPrivilege = userPrivilege;
    }

    /**
     * @return version
     */
    public Long getVersion() {
        return version;
    }

    /**
     * @param version セットする version
     */
    public void setVersion(Long version) {
        this.version = version;
    }

}
