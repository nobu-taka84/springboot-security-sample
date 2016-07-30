package com.springboot.security.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    /**
     * ログイン
     *
     * @return 遷移先テンプレート
     */
    @RequestMapping(value = "/login")
    public String login(HttpServletRequest request) {
        LOGGER.info("START:/login");

        Principal principal = request.getUserPrincipal();

        return (principal == null) ? "login" : "redirect:/";
    }

}
