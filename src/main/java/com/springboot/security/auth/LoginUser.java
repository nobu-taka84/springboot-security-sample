package com.springboot.security.auth;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.springboot.security.dao.dto.UserInfoDto;

@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class LoginUser {

    private UserInfoDto userInfo;

    /**
     * @return userInfo
     */
    public UserInfoDto getUserInfo() {
        return userInfo;
    }

    /**
     * @param userInfo セットする userInfo
     */
    public void setUserInfo(UserInfoDto userInfo) {
        if (userInfo != null) {
            userInfo.setPassword(null);
        }
        this.userInfo = userInfo;
    }

}
