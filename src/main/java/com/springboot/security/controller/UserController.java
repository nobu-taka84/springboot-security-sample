package com.springboot.security.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    /**
     * user
     *
     * @return 遷移先テンプレート
     */
    @RequestMapping(value = "/user/test")
    public String user() {
        LOGGER.info("START:/user/test");
        return "index";
    }

}
