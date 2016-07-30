package com.springboot.security.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.springboot.security.exception.BusinessException;
import com.springboot.security.exception.SystemException;

@Configuration
public class ExceptionHandler implements HandlerExceptionResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandler.class);

    /**
     * Exception処理
     * 
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param handler Object
     * @param e Exception
     * @return ModelAndView
     */
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response,
            Object handler, Exception e) {

        if (e instanceof BusinessException) {
            LOGGER.warn(e.getMessage(), e);
        } else {
            LOGGER.error(e.getMessage(), e);
        }

        // 画面遷移
        if (e instanceof SystemException) {
            // システム例外
            LOGGER.debug("instance=SystemException:setView=error");
            ModelAndView mav = new ModelAndView();
            mav.addObject("errorMsg", e.getMessage());
            mav.setViewName("error");
            return mav;
        }

        if (e instanceof BusinessException) {
            // ビジネス例外
            LOGGER.debug("instance=BusinessException:setView=error");
            ModelAndView mav = new ModelAndView();
            mav.addObject("errorMsg", e.getMessage());
            mav.setViewName("error");
            return mav;
        }

        LOGGER.debug("Exception:setView=null");
        return null;
    }

}
