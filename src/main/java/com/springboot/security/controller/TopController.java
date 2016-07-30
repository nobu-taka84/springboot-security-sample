package com.springboot.security.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TopController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TopController.class);

    /**
     * トップページ
     *
     * @param model
     * @param request
     * @return 遷移先テンプレート
     */
    @RequestMapping(value = "/")
    public String index(Model model, HttpServletRequest request) {
        LOGGER.info("START:/");

        Principal principal = request.getUserPrincipal();

        return (principal == null) ? "login" : "index";
    }

}
