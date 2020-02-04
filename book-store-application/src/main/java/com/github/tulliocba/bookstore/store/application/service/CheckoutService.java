package com.github.tulliocba.bookstore.store.application.service;

import com.github.tulliocba.bookstore.store.application.port.in.CheckoutUseCase;
import com.github.tulliocba.bookstore.store.application.port.out.CreateOrderPort;
import com.github.tulliocba.bookstore.store.application.port.out.LoadPromotionPort;
import com.github.tulliocba.bookstore.store.application.port.out.UpdateInventoryPort;
import com.github.tulliocba.bookstore.store.domain.Customer.CustomerId;
import com.github.tulliocba.bookstore.store.domain.Order;
import com.github.tulliocba.bookstore.store.domain.OrderItem;
import com.github.tulliocba.bookstore.store.domain.Promotion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Set;

import static org.springframework.util.StringUtils.isEmpty;

@Service
@Transactional
@RequiredArgsConstructor
public class CheckoutService implements CheckoutUseCase {

    final UpdateInventoryPort updateInventoryPort;

    final LoadPromotionPort loadPromotionPort;

    final CreateOrderPort createOrderPort;

    @Override
    public void checkout(final CheckoutCommand checkoutCommand) throws ItemUnavailableException, InvalidPromotionException {

        final Set<OrderItem> orderItems = checkoutCommand.toOrderItem();

        final Set<OrderItem> decrementedItems = updateInventoryPort.decrementInventory(orderItems);

        final Order order = new Order(decrementedItems, new CustomerId(checkoutCommand.getCustomerId()));

        if(!isEmpty(checkoutCommand.getPromotionCode())) {
            final Promotion promotion = loadPromotionPort.loadByCode(checkoutCommand.getPromotionCode());

            order.applyPromotion(promotion);
        }

        createOrderPort.save(order);
    }
}

