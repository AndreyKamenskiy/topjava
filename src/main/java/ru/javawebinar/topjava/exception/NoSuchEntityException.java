package ru.javawebinar.topjava.exception;

import javax.servlet.ServletException;

public class NoSuchEntityException extends ServletException {

    public NoSuchEntityException(String message) {
        super(message);
    }
}
