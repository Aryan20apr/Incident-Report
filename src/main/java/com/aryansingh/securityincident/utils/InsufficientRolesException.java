package com.aryansingh.securityincident.utils;

public class InsufficientRolesException extends RuntimeException{
    public InsufficientRolesException(String s) {
        super(s);
    }
}
