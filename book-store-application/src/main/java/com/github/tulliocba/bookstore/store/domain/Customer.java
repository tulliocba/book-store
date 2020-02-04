package com.github.tulliocba.bookstore.store.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.Value;

@EqualsAndHashCode(of = "id")
@ToString
public class Customer extends User {
    @Getter
    private CustomerId id;

    private boolean isMember;

    @Value
    public static class CustomerId {
        private String value;
    }
}
