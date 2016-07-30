package com.springboot.security.config;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.csrf.MissingCsrfTokenException;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.springboot.security.auth.CustomAuthenticationProvider;
import com.springboot.security.auth.CustomLoginUrlAuthenticationEntryPoint;
import com.springboot.security.constants.UserPrivilege;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    private CustomAuthenticationProvider authenticationProvider;

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

        // 権限でのアクセス制御
        http.authorizeRequests().antMatchers("/user/**").hasAnyRole(UserPrivilege.USER.name());

        // その他
        http.authorizeRequests().anyRequest().authenticated();

        // ログイン、ログアウト
        http.formLogin().loginPage("/login").defaultSuccessUrl("/", true)
                .failureHandler(authenticationFailureHandler);
        http.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/?logout").invalidateHttpSession(true);
        http.exceptionHandling()
                // 通常のRequestとAjaxを両方対応するSessionTimeout用
                .authenticationEntryPoint(authenticationEntryPoint())
                // csrfはsessionがないと動かない。SessionTimeout時にPOSTすると403 Forbiddenを必ず返してしまうため、
                // MissingCsrfTokenExceptionの時はリダイレクトを、それ以外の時は通常の扱いとする。
                .accessDeniedHandler(accessDeniedHandler());

        // CSRF設定
        http.csrf().requireCsrfProtectionMatcher(new CustomRequiresCsrfMatcher())
                .csrfTokenRepository(this.csrfTokenRepository());
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new CustomLoginUrlAuthenticationEntryPoint("/login");
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new AccessDeniedHandler() {
            @Override
            public void handle(HttpServletRequest request, HttpServletResponse response,
                    AccessDeniedException accessDeniedException)
                    throws IOException, ServletException {
                if (accessDeniedException instanceof MissingCsrfTokenException) {
                    authenticationEntryPoint().commence(request, response, null);
                } else {
                    new AccessDeniedHandlerImpl().handle(request, response, accessDeniedException);
                }
            }
        };
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

    private CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName("X-XSRF-TOKEN");
        return repository;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder authManagerBuilder) throws Exception {
        authManagerBuilder.authenticationProvider(authenticationProvider);
    }
}
