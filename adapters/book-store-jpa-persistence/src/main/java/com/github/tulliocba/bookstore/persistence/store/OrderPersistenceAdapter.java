package com.github.tulliocba.bookstore.persistence.store;

import com.github.tulliocba.bookstore.store.application.port.out.CreateOrderPort;
import com.github.tulliocba.bookstore.store.domain.Order;
import com.github.tulliocba.bookstore.store.domain.Promotion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class OrderPersistenceAdapter implements CreateOrderPort {

    private final OrderRepository orderRepository;

    private final PromotionRepository promotionRepository;

    private final UserRepository userRepository;

    @Override
    public void save(Order order) {
        final Optional<Promotion> promotion = promotionRepository.findByCode(order.getPromotion().getCode());

        final Optional<UserEntity> customer = userRepository.findById(order.getCustomerId().getValue());




        orderRepository.save(OrderEntity.toEntity(order));
    }

    public Long loadById(Long id) {
        return orderRepository.findById(id).get().getId();

    }
}
