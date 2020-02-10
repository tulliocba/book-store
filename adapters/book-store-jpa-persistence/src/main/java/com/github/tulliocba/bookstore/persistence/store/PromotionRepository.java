package com.github.tulliocba.bookstore.persistence.store;

import com.github.tulliocba.bookstore.store.domain.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface PromotionRepository extends JpaRepository<PromotionEntity, Long> {
    Optional<Promotion> findByCode(String code);
}
