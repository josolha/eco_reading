package com.checkcheck.ecoreading.domain.users.exception;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class AuthenticationEmailException extends UsernameNotFoundException {

    public AuthenticationEmailException(String message) {
        super(message);
    }
}
