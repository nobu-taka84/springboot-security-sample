package com.springboot.security.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    /**
     * ログイン
     *
     * @return 遷移先テンプレート
     */
    @RequestMapping(value = "/login")
    public String login() {
        LOGGER.info("START:/login");
        return "login";
    }

    /**
     * ログアウト
     *
     * @return 遷移先テンプレート
     */
    @RequestMapping(value = "/", method = RequestMethod.GET, params = {"logout"})
    public String logout() {
        LOGGER.info("START:/logout");
        return "login";
    }

}
