package com.github.tulliocba.bookstore.persistence.store;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface PromotionRepository extends JpaRepository<PromotionEntity, Long> {
    Optional<PromotionEntity> findByCode(String code);
}
