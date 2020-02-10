package com.github.tulliocba.bookstore.persistence.store;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "promotion")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class PromotionEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String code;

    private Integer percentage;

    private LocalDateTime expiration;
}
