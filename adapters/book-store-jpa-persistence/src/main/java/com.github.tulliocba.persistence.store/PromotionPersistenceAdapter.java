package com.github.tulliocba.persistence.store;

import com.github.tulliocba.bookstore.store.application.port.out.LoadPromotionPort;
import com.github.tulliocba.bookstore.store.application.service.PromotionCodeNotFoundException;
import com.github.tulliocba.bookstore.store.domain.Promotion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class PromotionPersistenceAdapter implements LoadPromotionPort {

    private final PromotionRepository promotionRepository;

    @Override
    public Promotion loadByCode(String code) throws PromotionCodeNotFoundException {
        return promotionRepository.findByCode(code)
                .orElseThrow(() ->
                        new PromotionCodeNotFoundException("Promotion with code "+ code+ "does not found"));
    }
}
