package com.springboot.security.dao.dto;

import java.io.Serializable;

public class UserInfoIdGeneratorDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String pk;

    private Long value;

    public UserInfoIdGeneratorDto() {
    }

    public String getPk() {
        return this.pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    public Long getValue() {
        return this.value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

}
