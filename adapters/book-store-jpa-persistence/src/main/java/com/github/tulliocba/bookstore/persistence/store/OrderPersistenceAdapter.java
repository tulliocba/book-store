package com.github.tulliocba.bookstore.persistence.store;

import com.github.tulliocba.bookstore.store.application.port.out.CreateOrderPort;
import com.github.tulliocba.bookstore.store.domain.Customer;
import com.github.tulliocba.bookstore.store.domain.Customer.CustomerId;
import com.github.tulliocba.bookstore.store.domain.Order;
import com.github.tulliocba.bookstore.store.domain.Order.OrderId;
import com.github.tulliocba.bookstore.store.domain.OrderItem;
import com.github.tulliocba.bookstore.store.domain.OrderItem.OrderItemId;
import com.github.tulliocba.bookstore.store.domain.Promotion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class OrderPersistenceAdapter implements CreateOrderPort {

    private final OrderRepository orderRepository;

    private final PromotionRepository promotionRepository;

    private final UserRepository userRepository;

    @Override
    public void save(Order order) {
        final Optional<PromotionEntity> promotion = promotionRepository.findByCode(order.getPromotion().getCode());

        final Optional<UserEntity> customer = userRepository.findById(order.getCustomerId().getValue());

        final OrderEntity savedOrder = orderRepository.save(new OrderEntity(order, promotion, customer));
    }

    public Order loadById(Long id) {
        final Optional<OrderEntity> optionalOrder = orderRepository.findById(id);

        final OrderEntity orderEntity = optionalOrder.orElseThrow(() -> new NoResultException("Order does not found"));

        final Set<OrderItem> items = orderEntity.getItems().stream().map(item ->
                OrderItem.with(new OrderItemId(item.getId()), item.getInventoryItem().getPrice(), item.getQuantity()))
                .collect(Collectors.toSet());

       return new Order(new OrderId(orderEntity.getId()), new CustomerId(orderEntity.getCustomer().getId()),
                items, orderEntity.getTotal(),
                new Promotion(orderEntity.getPromotion().getCode(), orderEntity.getPromotion().getPercentage(),
                        orderEntity.getPromotion().getExpiration()));

    }
}
