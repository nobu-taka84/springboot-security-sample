package com.springboot.security.dao.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "user_info_id_generator")
@NamedQuery(name = "UserInfoIdGenerator.findAll", query = "SELECT a FROM UserInfoIdGenerator a")
public class UserInfoIdGenerator implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String pk;

    private Long value;

    public UserInfoIdGenerator() {}

    /**
     * @return pk
     */
    public String getPk() {
        return pk;
    }

    /**
     * @param pk セットする pk
     */
    public void setPk(String pk) {
        this.pk = pk;
    }

    /**
     * @return value
     */
    public Long getValue() {
        return value;
    }

    /**
     * @param value セットする value
     */
    public void setValue(Long value) {
        this.value = value;
    }

}
