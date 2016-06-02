package com.springboot.security.config;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.springboot.security.auth.CustomAuthenticationProvider;
import com.springboot.security.auth.CustomLogoutHandler;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    private CustomAuthenticationProvider authenticationProvider;

    @Autowired
    private CustomLogoutHandler logoutHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/").permitAll();
        http.authorizeRequests().antMatchers("/login").permitAll();
        http.authorizeRequests().antMatchers("/account/join").permitAll();
        http.authorizeRequests().antMatchers("/account/create").permitAll();
        http.authorizeRequests().antMatchers("/error").permitAll();
        http.authorizeRequests().antMatchers("/css/**").permitAll();
        http.authorizeRequests().antMatchers("/img/**").permitAll();
        http.authorizeRequests().antMatchers("/js/**").permitAll();
        http.authorizeRequests().anyRequest().authenticated();

        // ログイン、ログアウト
        http.formLogin().loginPage("/login").defaultSuccessUrl("/", true)
                .failureHandler(authenticationFailureHandler);
        http.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .addLogoutHandler(logoutHandler).logoutSuccessUrl("/?logout")
                .invalidateHttpSession(true);

        // CSRF設定
        http.csrf().requireCsrfProtectionMatcher(new CustomRequiresCsrfMatcher());
    }

    private class CustomRequiresCsrfMatcher implements RequestMatcher {
        // デフォルトではGET、HEAD、TRACE、OPTIONS以外のメソッドをチェックしている
        private Pattern allowedMethods = Pattern.compile("^(GET|HEAD|TRACE|OPTIONS)$");
        private Pattern allowedApis = Pattern.compile("^/authenticate/.+$");

        @Override
        public boolean matches(HttpServletRequest request) {
            // マッチするものだけチェックの対象とする
            return !allowedApis.matcher(request.getRequestURI()).matches()
                    && !allowedMethods.matcher(request.getMethod()).matches();
        }
    }

    @Override
    protected void configure(AuthenticationManagerBuilder authManagerBuilder) throws Exception {
        authManagerBuilder.authenticationProvider(authenticationProvider);
    }
}
