package com.github.tulliocba.persistence.store;

import com.github.tulliocba.bookstore.store.application.port.out.LoadPromotionPort;
import com.github.tulliocba.bookstore.store.application.service.PromotionCodeNotFoundException;
import com.github.tulliocba.bookstore.store.domain.Promotion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PromotionPersistenceAdapter implements LoadPromotionPort {

    @Override
    public Promotion loadByCode(String code) throws PromotionCodeNotFoundException {
        return null;
    }
}
