package com.checkcheck.ecoreading.domain.users.exception;

import java.io.IOException;
import java.net.URLEncoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        String encodedErrorMessage = URLEncoder.encode(exception.getMessage(), "UTF-8");

        //1.카카오 이미있는 회원
        if (exception instanceof AuthenticationEmailException) {
            log.info("encodeErroe"+encodedErrorMessage);
            response.sendRedirect("/user/login?error=" + encodedErrorMessage);
        } else {
            // 다른 종류의 인증 실패 처리
        }
        // 기본적인 실패 처리
    }
}
