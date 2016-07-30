package com.springboot.security.controller;

import java.util.Locale;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import com.springboot.security.auth.CustomAuthenticationProvider;
import com.springboot.security.auth.CustomUserDetailsService;
import com.springboot.security.constants.ErrorCode;
import com.springboot.security.dao.dto.UserInfoDto;
import com.springboot.security.exception.UserInfoException;
import com.springboot.security.form.ChangePasswordForm;
import com.springboot.security.form.JoinForm;
import com.springboot.security.service.AuthService;
import com.springboot.security.service.UserInfoService;

@Controller
@RequestMapping(value = "/account")
public class AccountController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private CustomAuthenticationProvider authenticationProvider;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;


    /**
     * 会員登録フォーム表示
     *
     * @return 遷移先テンプレート
     */
    @RequestMapping(value = "/join")
    public String join(Model model) {
        LOGGER.info("START:/account/join");
        model.addAttribute("joinForm", new JoinForm());
        model.addAttribute("result", "");
        return "account/join";
    }

    /**
     * 会員登録
     *
     * @return 遷移先テンプレート
     */
    @Transactional
    @RequestMapping(value = "/create")
    public String create(Model model, @Valid JoinForm form, BindingResult result, Locale locale) {
        LOGGER.info("START:/account/create");

        if (result.hasErrors()) {
            model.addAttribute("formErrors", result.getAllErrors());
            return "account/join";
        }
        if (!form.getPassword().equals(form.getConfirmPassword())) {
            String message = messageSource.getMessage("passwordsDoNotMatch", null, locale);
            result.reject("ERROR", message);
            model.addAttribute("checkErrors", result.getAllErrors());
            model.addAttribute("joinForm", form);
            return "account/join";
        }

        try {
            authService.checkUserName(form.getUsername());
            authService.checkPassword(form.getPassword());
        } catch (UserInfoException e) {
            String message = "";
            switch (e.getUserInfoErrorType()) {
                case USER_NAME_TOO_SHORT:
                    message = messageSource.getMessage("userNameIsTooShort", null, locale);
                    break;
                case USER_NAME_TOO_LONG:
                    message = messageSource.getMessage("userNameIsTooLong", null, locale);
                    break;
                case ID_OR_PASSWORD_INVALID:
                    message = messageSource.getMessage("userNameInvalid", null, locale);
                    break;
                case PASSWORD_TOO_SHORT:
                    message = messageSource.getMessage("passwordIsTooShort", null, locale);
                    break;
                case PASSWORD_TOO_LONG:
                    message = messageSource.getMessage("passwordIsTooLong", null, locale);
                    break;
                case PASSWORD_UNCOMPLICATED:
                    message = messageSource.getMessage("passwordIsUncomplicated", null, locale);
                    break;
                default:
                    message = ErrorCode.UNEXPECTEDINPUT_GENERAL.name();
                    break;
            }
            result.reject("ERROR", message);
            model.addAttribute("checkErrors", result.getAllErrors());
            model.addAttribute("joinForm", form);
            return "account/join";
        }

        if (userInfoService.isUsernameExist(form.getUsername())) {
            String message = messageSource.getMessage("userAlreadyExists", null, locale);
            result.reject("ERROR", message);
            model.addAttribute("checkErrors", result.getAllErrors());
            model.addAttribute("joinForm", new JoinForm());
            return "account/join";
        }

        // 会員登録
        UserInfoDto createUserInfo =
                userInfoService.createUserInfo(form.getUsername(), form.getPassword());

        // 登録した会員でログイン
        createUserInfo.setPassword("");
        autoLogin(createUserInfo);

        return "redirect:/";
    }

    /**
     * 登録完了のユーザー情報を使って、強制的にログインを通す
     * 
     * @param userInfo 登録したユーザーの情報
     * @param request リクエスト
     * @param response レスポンス
     */
    private void autoLogin(UserInfoDto userInfo) {
        try {
            Authentication authentication = authenticationProvider.authenticate(userInfo);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            LOGGER.warn("ユーザー登録後の自動ログインに失敗", e);
            SecurityContextHolder.getContext().setAuthentication(null);
            return;
        }
    }

    /**
     * パスワード変更フォーム表示
     *
     * @return 遷移先テンプレート
     */
    @RequestMapping(value = "/changepassword")
    public String changePassword(Model model) {
        LOGGER.info("START:/account/changepassword");
        model.addAttribute("joinForm", new JoinForm());
        model.addAttribute("result", "");
        return "account/changepassword";
    }

    /**
     * パスワード変更登録
     *
     * @return 遷移先テンプレート
     */
    @RequestMapping(value = "/updatepassword")
    public String updatePassword(Model model, @Valid ChangePasswordForm form, BindingResult result,
            Locale locale) {
        LOGGER.info("START:/account/updatepassword");

        if (result.hasErrors()) {
            model.addAttribute("formErrors", result.getAllErrors());
            return "account/changepassword";
        }
        if (!form.getPassword().equals(form.getConfirmPassword())) {
            String message = messageSource.getMessage("passwordsDoNotMatch", null, locale);
            result.reject("ERROR", message);
            model.addAttribute("checkErrors", result.getAllErrors());
            model.addAttribute("joinForm", form);
            return "account/changepassword";
        }

        try {
            authService.checkPassword(form.getPassword());
        } catch (UserInfoException e) {
            String message = "";
            switch (e.getUserInfoErrorType()) {
                case PASSWORD_TOO_SHORT:
                    message = messageSource.getMessage("passwordIsTooShort", null, locale);
                    break;
                case PASSWORD_TOO_LONG:
                    message = messageSource.getMessage("passwordIsTooLong", null, locale);
                    break;
                case PASSWORD_UNCOMPLICATED:
                    message = messageSource.getMessage("passwordIsUncomplicated", null, locale);
                    break;
                default:
                    message = ErrorCode.UNEXPECTEDINPUT_GENERAL.name();
                    break;
            }
            result.reject("ERROR", message);
            model.addAttribute("checkErrors", result.getAllErrors());
            model.addAttribute("joinForm", form);
            return "account/changepassword";
        }

        // ユーザ情報取得
        UserInfoDto userInfo = customUserDetailsService.getAuthUserInfo();

        // パスワード変更
        userInfoService.updatePassword(userInfo.getUsername(), form.getPassword());

        return "redirect:/";
    }

}
