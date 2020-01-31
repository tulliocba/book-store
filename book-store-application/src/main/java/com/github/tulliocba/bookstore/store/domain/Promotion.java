package com.github.tulliocba.bookstore.store.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@EqualsAndHashCode(of = "code")
@ToString
public class Promotion {
    private String code;
    @Getter
    private int percentage;
    private LocalDateTime expiration;
}
