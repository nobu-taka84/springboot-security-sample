package com.springboot.security.auth;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.springboot.security.constants.UserPrivilege;
import com.springboot.security.dao.dto.UserInfoDto;
import com.springboot.security.dao.entity.LoginHistory;
import com.springboot.security.exception.UserInfoException;
import com.springboot.security.service.AuthService;
import com.springboot.security.service.UserInfoService;

@Component(value = "authenticationProvider")
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(CustomAuthenticationProvider.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    /**
     * ログイン処理
     */
    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        // 認証API
        UserInfoDto userInfo = new UserInfoDto();
        try {
            userInfo = authService.authenticate(auth.getName(), auth.getCredentials().toString());
        } catch (UserInfoException userInfoException) {
            LOGGER.warn("■ Exception[" + userInfoException + "]");
            switch (userInfoException.getUserInfoErrorType()) {
                case USER_NOT_EXISTS:
                    throw new BadCredentialsException("認証エラー（ユーザーが存在しない）");
                case ACCOUNT_LOCKED:
                    throw new LockedException("認証エラー（アカウントロック）");
                case PASSWORD_EXPIRED:
                    throw new AccountExpiredException("認証エラー（パスワード期限切れ）");
                case ID_OR_PASSWORD_INVALID:
                    throw new BadCredentialsException("認証エラー（ユーザIDまたはパスワードが不正）");
                default:
                    throw new BadCredentialsException("認証エラー発生");
            }
        } catch (Exception exception) {
            LOGGER.error("■ Exception[" + exception + "]");
            throw new BadCredentialsException("想定外エラー発生");
        }

        if (userInfo == null) {
            LOGGER.error("■ userInfo[ null ]");
            throw new AuthenticationServiceException("UserInfoService returned null");
        }

        // ログイン履歴更新
        updateLoginHistory(userInfo.getId());

        // 権限付与
        userInfo.setPassword("");
        return setUserPrivilege(userInfo);
    }

    /**
     * ログイン履歴更新
     */
    private void updateLoginHistory(Long userInfoId) {
        // 前回ログイン日時更新
        userInfoService.updateLastLoginedAt(userInfoId);

        // ログイン履歴更新
        LoginHistory loginHistory = new LoginHistory();
        loginHistory.setUserInfoId(userInfoId);
        loginHistory.setIpAddress(request.getRemoteAddr());
        loginHistory.setUserAgent(request.getHeader("user-agent"));
        loginHistory.setLoginedAt(new Timestamp(System.currentTimeMillis()));
        userInfoService.updateLoginHistory(loginHistory);
    }

    /**
     * ログイン処理
     */
    public Authentication authenticate(UserInfoDto userInfo) throws AuthenticationException {
        // ログイン履歴更新
        updateLoginHistory(userInfo.getId());

        // 権限付与
        return setUserPrivilege(userInfo);
    }

    /**
     * 権限付与
     */
    public Authentication setUserPrivilege(UserInfoDto userInfo) throws AuthenticationException {
        // 権限付与
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        userInfo.getUserPrivilegeInfoList().forEach(privilege -> {
            authorityList.add(new SimpleGrantedAuthority(UserPrivilege.getUserRole(privilege)));
        });

        // ログインユーザーをセット
        UserDetails loadedUser = userDetailsService.setUserInfo(userInfo, authorityList);

        return new UsernamePasswordAuthenticationToken(loadedUser, null, authorityList);
    }

    @Override
    public boolean supports(Class<?> auth) {
        return true;
    }

}
