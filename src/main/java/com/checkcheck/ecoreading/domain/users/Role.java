package com.checkcheck.ecoreading.domain.users;

public enum Role {

    USER,
    ADMIN;

    @Override
    public String toString() {
        return "ROLE_" + name();
    }
}
