package com.bookorder.management.exception;

public class BookOrderException extends Exception {

    public BookOrderException() {
        super();
    }

    public BookOrderException(String message) {
        super(message);
    }

    public BookOrderException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
