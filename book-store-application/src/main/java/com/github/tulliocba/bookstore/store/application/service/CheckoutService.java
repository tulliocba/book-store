package com.github.tulliocba.bookstore.store.application.service;

import com.github.tulliocba.bookstore.store.application.port.in.CheckoutUseCase;
import com.github.tulliocba.bookstore.store.application.port.out.CreateOrderPort;
import com.github.tulliocba.bookstore.store.application.port.out.LoadInventoryPort;
import com.github.tulliocba.bookstore.store.application.port.out.LoadPromotionPort;
import com.github.tulliocba.bookstore.store.application.port.out.UpdateInventoryPort;
import com.github.tulliocba.bookstore.store.domain.Customer.CustomerId;
import com.github.tulliocba.bookstore.store.domain.InventoryItem;
import com.github.tulliocba.bookstore.store.domain.InventoryItem.InventoryItemId;
import com.github.tulliocba.bookstore.store.domain.Order;
import com.github.tulliocba.bookstore.store.domain.OrderItem;
import com.github.tulliocba.bookstore.store.domain.Promotion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.isEmpty;

@Service
@Transactional
@RequiredArgsConstructor
public class CheckoutService implements CheckoutUseCase {

    final UpdateInventoryPort updateInventoryPort;

    final LoadPromotionPort loadPromotionPort;

    final CreateOrderPort createOrderPort;

    final LoadInventoryPort loadInventoryPort;

    @Override
    public void checkout(final CheckoutCommand checkoutCommand)
            throws ItemUnavailableException, PromotionCodeNotFoundException {

        final Set<InventoryItem> inventoryItems = loadInventoryPort.loadItemsById(getOrderItemsId(checkoutCommand));

        final Set<OrderItem> orderItems =
                checkStockAvailabilityAndCreateItemOrder(checkoutCommand, inventoryItems);

        updateInventoryPort.update(inventoryItems);

        final Order order = new Order(orderItems, new CustomerId(checkoutCommand.getCustomerId()));

        applyPromotionCode(checkoutCommand, order);

        createOrderPort.save(order);
    }

    private void applyPromotionCode(CheckoutCommand checkoutCommand,
                                    Order order) throws PromotionCodeNotFoundException {

        if(!isEmpty(checkoutCommand.getPromotionCode())) {
            final Promotion promotion = loadPromotionPort.loadByCode(checkoutCommand.getPromotionCode());

            order.applyPromotion(promotion);
        }
    }

    private Set<OrderItem> checkStockAvailabilityAndCreateItemOrder(CheckoutCommand checkoutCommand,
                                                                    Set<InventoryItem> inventoryItems) throws ItemUnavailableException {

        final Set<OrderItem> orderItems = new HashSet<>();

        for (InventoryItem item : inventoryItems) {

            final Item commandItem = getCommandItem(checkoutCommand, item);

            item.decrementStock(commandItem.getQuantity());

            orderItems.add(createOrderItem(item, commandItem));
        }

        return orderItems;
    }

    private Item getCommandItem(CheckoutCommand checkoutCommand, InventoryItem item) throws ItemUnavailableException {
        return checkoutCommand.getItems().stream().filter(cmdItem -> cmdItem.getItemId().equals(item.getId().getValue()))
                .findFirst().orElseThrow(() -> new ItemUnavailableException("The item of id: "+ item.getId().getValue() +"does not found"));
    }

    private OrderItem createOrderItem(InventoryItem item, Item commandItem) {
        return OrderItem.with(new OrderItem.OrderItemId(item.getId().getValue()), item.getPrice(), commandItem.getQuantity());
    }

    private Set<InventoryItemId> getOrderItemsId(CheckoutCommand checkoutCommand) {
        return checkoutCommand.getItems().stream().map(item -> new InventoryItemId(item.getItemId())).collect(Collectors.toSet());
    }
}

