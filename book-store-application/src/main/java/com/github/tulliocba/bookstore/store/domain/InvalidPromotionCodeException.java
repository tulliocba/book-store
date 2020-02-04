package com.github.tulliocba.bookstore.store.domain;

public class InvalidPromotionCodeException extends RuntimeException {
    public InvalidPromotionCodeException(String message) {
        super(message);
    }
}
