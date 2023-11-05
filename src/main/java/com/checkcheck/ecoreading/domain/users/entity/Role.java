package com.checkcheck.ecoreading.domain.users.entity;

public enum Role {

    USER,
    ADMIN;

    @Override
    public String toString() {
        return "ROLE_" + name();
    }
}
