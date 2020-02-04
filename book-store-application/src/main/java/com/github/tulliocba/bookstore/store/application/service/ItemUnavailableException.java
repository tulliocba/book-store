package com.github.tulliocba.bookstore.store.application.service;

public class ItemUnavailableException extends Exception {
    public ItemUnavailableException() {
        super("There is one or more items not available");
    }

    public ItemUnavailableException(String message) {
        super(message);
    }
}
