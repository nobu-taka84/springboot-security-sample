package com.springboot.security.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EncryptUtils {

    @Value("${auth.passwordEncryption.key}")
    private String key = "";

    @Value("${auth.passwordEncryption.iv}")
    private String iv = "";

    /**
     * 暗号化key取得
     * 
     * @return key
     */
    public String getKey() {
        return key;
    }

    /**
     * 暗号化iv取得
     * 
     * @return iv
     */
    public String getIv() {
        return iv;
    }

}
