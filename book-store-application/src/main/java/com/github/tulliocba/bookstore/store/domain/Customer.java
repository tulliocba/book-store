package com.github.tulliocba.bookstore.store.domain;

import lombok.Value;

public class Customer extends User {
    private CustomerId id;
    private boolean isMember;

    @Value
    public static class CustomerId {
        private String value;
    }
}
