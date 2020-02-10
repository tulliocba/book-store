package com.github.tulliocba.bookstore.persistence.store;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "promotion")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PromotionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private Integer percentage;

    private LocalDateTime expiration;
}
