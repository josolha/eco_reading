package com.checkcheck.ecoreading.domain.users.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


@Slf4j
public class AuthenticationEmailException extends UsernameNotFoundException {
    public AuthenticationEmailException(String message) {
        super(message);
        log.info("message : "+ message);
    }
}
