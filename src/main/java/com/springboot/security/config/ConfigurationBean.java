package com.springboot.security.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.ExceptionMappingAuthenticationFailureHandler;

@Configuration
public class ConfigurationBean {

    /**
     * AuthenticationFailureHandler
     *
     * @return 認証失敗時のハンドラー
     */
    @Bean(name = "authenticationFailureHandler")
    public AuthenticationFailureHandler defaultAuthenticationFailureHandler() {

        // Exceptionと遷移先のマッピング
        Map<String, String> exceptionMappings = new HashMap<>();

        // パスワード期限切れ
        exceptionMappings.put(//
                AccountExpiredException.class.getCanonicalName(), "/login?error=expired");

        // アカウントロック
        exceptionMappings.put(//
                LockedException.class.getCanonicalName(), "/login?error=locked");

        final ExceptionMappingAuthenticationFailureHandler result =
                new ExceptionMappingAuthenticationFailureHandler();
        result.setExceptionMappings(exceptionMappings);

        // その他の認証エラー
        result.setDefaultFailureUrl("/login?error=default");

        return result;
    }

    /**
     * messageSource
     *
     * @return messageSource
     */
    @Bean(name = "messageSource")
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource bean = new ReloadableResourceBundleMessageSource();
        bean.setBasename("classpath:messages");
        bean.setDefaultEncoding("UTF-8");
        return bean;
    }

}
