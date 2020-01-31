package com.github.tulliocba.bookstore.store.application.service;

public class ItemUnavailableException extends Exception {
    public ItemUnavailableException(String message) {
        super(message);
    }
}
