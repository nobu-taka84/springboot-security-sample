package com.springboot.security.auth;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.springboot.security.dao.dto.UserInfoDto;
import com.springboot.security.model.LoginUser;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    public UserDetails setUserInfo(UserInfoDto userInfo,
            List<SimpleGrantedAuthority> authorityList) {
        return new LoginUser(userInfo, authorityList);
    }

    public UserInfoDto getAuthUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (authentication.getPrincipal() instanceof LoginUser) ? //
                ((LoginUser) authentication.getPrincipal()).getUserInfo() : null;
    }

    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (authentication.getPrincipal() instanceof LoginUser) ? //
                authentication.isAuthenticated() : false;
    }

}
