package com.github.tulliocba.bookstore.store.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@EqualsAndHashCode(of = "code")
@ToString
@AllArgsConstructor
public class Promotion {
    @Getter
    private String code;
    @Getter
    private int percentage;
    @Getter
    private LocalDateTime expiration;
}
