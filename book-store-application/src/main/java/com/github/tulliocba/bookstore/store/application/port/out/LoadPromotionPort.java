package com.github.tulliocba.bookstore.store.application.port.out;

import com.github.tulliocba.bookstore.store.application.service.PromotionCodeNotFoundException;
import com.github.tulliocba.bookstore.store.domain.Promotion;

public interface LoadPromotionPort {

    Promotion loadByCode(String code) throws PromotionCodeNotFoundException;
}
