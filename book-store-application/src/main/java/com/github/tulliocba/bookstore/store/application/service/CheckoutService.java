package com.github.tulliocba.bookstore.store.application.service;

import com.github.tulliocba.bookstore.store.application.port.in.CheckoutUseCase;
import com.github.tulliocba.bookstore.store.application.port.out.CreateOrderPort;
import com.github.tulliocba.bookstore.store.application.port.out.UpdateInventoryPort;
import com.github.tulliocba.bookstore.store.application.port.out.LoadPromotionPort;
import com.github.tulliocba.bookstore.store.domain.Order;
import com.github.tulliocba.bookstore.store.domain.OrderItem;
import com.github.tulliocba.bookstore.store.domain.Promotion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CheckoutService implements CheckoutUseCase {

    final UpdateInventoryPort updateInventoryPort;

    final LoadPromotionPort loadPromotionPort;

    final CreateOrderPort createOrderPort;

    @Override
    public void checkout(final CheckoutCommand checkoutCommand) throws ItemUnavailableException, InvalidPromotionException {

        final Set<OrderItem> orderItems = getOrderItems(checkoutCommand);

        final Set<OrderItem> itemsUpdated = updateInventoryPort.decrementInventory(orderItems);

        final Order order = Order.withItems(itemsUpdated);


        if(!StringUtils.isEmpty(checkoutCommand.getPromotionCode())) {
            final Promotion promotion = loadPromotionPort.loadByCode(checkoutCommand.getPromotionCode());

            order.applyPromotion(promotion);
        }

        createOrderPort.save(order);
    }

    private Set<OrderItem> getOrderItems(CheckoutCommand checkoutCommand) {
        return checkoutCommand.getItems()
                .stream()
                .map(item -> OrderItem.with(new OrderItem.OrderItemId(item.getItemId()), item.getQuantity()))
                .collect(Collectors.toSet());
    }
}

