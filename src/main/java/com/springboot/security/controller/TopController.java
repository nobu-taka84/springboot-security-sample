package com.springboot.security.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.springboot.security.auth.LoginUser;

@Controller
public class TopController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TopController.class);

    @Autowired
    private LoginUser loginUser;

    /**
     * トップページ
     *
     * @return 遷移先テンプレート
     */
    @RequestMapping(value = "/")
    public String index() {
        LOGGER.info("START:/");

        if (loginUser.getUserInfo() == null) {
            return "login";
        }

        return "index";
    }

}
