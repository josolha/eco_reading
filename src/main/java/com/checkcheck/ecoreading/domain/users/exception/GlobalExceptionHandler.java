package com.checkcheck.ecoreading.domain.users.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler({AuthenticationServiceException.class, UsernameNotFoundException.class})
    public String handleAuthenticationException(RuntimeException ex, RedirectAttributes redirectAttributes){
        log.info("ex.getMessage() = " + ex.getMessage());
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        System.out.println("ex.getMessage() = " + ex.getMessage());
        return "redirect:/user/login"; // 로그인 페이지로 리다이렉트
    }
}
