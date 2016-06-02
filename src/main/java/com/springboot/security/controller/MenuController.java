package com.springboot.security.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.springboot.security.auth.LoginUser;

@Controller
@RequestMapping(value = "/menu")
public class MenuController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MenuController.class);

    @Autowired
    private LoginUser loginUser;

    /**
     * メニュー表示
     *
     * @return 遷移先テンプレート
     */
    @RequestMapping(value = "/list")
    public String index(Model model) {
        LOGGER.debug("START:/menu/list");
        model.addAttribute("loginUser", loginUser.getUserInfo().getUsername());
        return "menu/list";
    }

}
