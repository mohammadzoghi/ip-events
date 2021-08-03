package com.mohammad.ipevents.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AppNotFoundException extends RuntimeException {

    public AppNotFoundException(String message) {
        super(message);
    }
}
