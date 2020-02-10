package com.github.tulliocba.bookstore.persistence.store;

import com.github.tulliocba.bookstore.store.application.service.PromotionCodeNotFoundException;
import com.github.tulliocba.bookstore.store.domain.Promotion;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Import({PromotionPersistenceAdapter.class})
public class PromotionPersistenceAdapterTest {

    @Autowired
    private PromotionPersistenceAdapter adapter;

    @Test
    @Sql("CreatePromotion.sql")
    void should_load_promotion_by_code() throws PromotionCodeNotFoundException {
        final String code_test = "CODE_TEST";
        final Promotion promotion = adapter.loadByCode(code_test);

        Assertions.assertThat(promotion.getCode()).isEqualTo(code_test);
    }
}
