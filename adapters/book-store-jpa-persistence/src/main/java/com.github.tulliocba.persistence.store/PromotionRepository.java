package com.github.tulliocba.persistence.store;

import com.github.tulliocba.bookstore.store.domain.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PromotionRepository extends JpaRepository<PromotionEntity, Long> {

    Optional<Promotion> findByCode(String code);
}
