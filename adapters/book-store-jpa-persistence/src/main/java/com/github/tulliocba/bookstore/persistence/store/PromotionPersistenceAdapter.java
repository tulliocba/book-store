package com.github.tulliocba.bookstore.persistence.store;

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
        final PromotionEntity promotionEntity = promotionRepository.findByCode(code)
                .orElseThrow(() ->
                        new PromotionCodeNotFoundException("Promotion with code " + code + "does not found"));
        return new Promotion(promotionEntity.getCode(), promotionEntity.getPercentage(), promotionEntity.getExpiration());
    }
}
