package com.github.tulliocba.bookstore.store.application.service;

import com.github.tulliocba.bookstore.store.application.port.in.CheckoutUseCase.CheckoutCommand;
import com.github.tulliocba.bookstore.store.application.port.out.CreateOrderPort;
import com.github.tulliocba.bookstore.store.application.port.out.LoadInventoryPort;
import com.github.tulliocba.bookstore.store.application.port.out.LoadPromotionPort;
import com.github.tulliocba.bookstore.store.application.port.out.UpdateInventoryPort;
import com.github.tulliocba.bookstore.store.domain.Customer.CustomerId;
import com.github.tulliocba.bookstore.store.domain.InventoryItem;
import com.github.tulliocba.bookstore.store.domain.InventoryItem.InventoryItemId;
import com.github.tulliocba.bookstore.store.domain.Order;
import com.github.tulliocba.bookstore.store.domain.OrderItem;
import com.github.tulliocba.bookstore.store.domain.OrderItem.OrderItemId;
import com.github.tulliocba.bookstore.store.domain.Promotion;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.github.tulliocba.bookstore.store.application.port.in.CheckoutUseCase.Item;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CheckoutService.class)
public class CheckoutServiceTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private LoadPromotionPort loadPromotionPort;
    private CreateOrderPort createOrderPort;
    private UpdateInventoryPort updateInventoryPort;
    private LoadInventoryPort loadInventoryPort;
    private CheckoutService checkoutService;
    private Set<Item> items;

    private static int DEFAULT_STOCK = 10;

    @Before
    public void setUp() {
        loadPromotionPort = mock(LoadPromotionPort.class);
        createOrderPort = mock(CreateOrderPort.class);
        updateInventoryPort = mock(UpdateInventoryPort.class);
        loadInventoryPort = mock(LoadInventoryPort.class);

        checkoutService = new CheckoutService(updateInventoryPort, loadPromotionPort,
                createOrderPort, loadInventoryPort);

        items = createItemsCommand();
    }

    @Test
    public void should_succeeds_checkout_without_promotion_code() throws Exception {

        CheckoutCommand command = new CheckoutCommand(randomUUID().toString(), items, null);

        final Set<InventoryItem> inventoryItems = getInventoryItems();

        given(loadInventoryPort.loadItemsById(mapToInventoryItemId()))
            .willReturn(inventoryItems);

        final Order mockedOrder = mock(Order.class);

        whenNew(Order.class)
                .withArguments(getOrderItems(), new CustomerId(command.getCustomerId()))
                .thenReturn(mockedOrder);

        checkoutService.checkout(command);

        thenAssertThatInventoryItemHasBeenDecremented(inventoryItems);

        then(updateInventoryPort).should(times(1))
                .update(inventoryItems);

        then(loadPromotionPort).should(never())
                .loadByCode(command.getPromotionCode());

        assertThat(mockedOrder).isNotNull();

        then(mockedOrder).should(never()).applyPromotion(any(Promotion.class));

        then(createOrderPort).should(times(1)).save(mockedOrder);
    }

    @Test
    public void should_succeeds_checkout_with_promotion_code() throws Exception {

        final String promotionCode = randomUUID().toString();

        CheckoutCommand command = new CheckoutCommand(randomUUID().toString(), items, promotionCode);

        final Set<InventoryItem> inventoryItems = getInventoryItems();

        given(loadInventoryPort.loadItemsById(mapToInventoryItemId()))
                .willReturn(inventoryItems);

        final Promotion promotion = new Promotion(promotionCode, 10, LocalDateTime.now().plusDays(1));

        given(loadPromotionPort.loadByCode(command.getPromotionCode()))
                .willReturn(promotion);

        final Order mockedOrder = mock(Order.class);

        whenNew(Order.class)
                .withArguments(getOrderItems(), new CustomerId(command.getCustomerId()))
                .thenReturn(mockedOrder);

        checkoutService.checkout(command);

        thenAssertThatInventoryItemHasBeenDecremented(inventoryItems);

        then(updateInventoryPort).should(times(1))
                .update(inventoryItems);

        then(loadPromotionPort).should(times(1))
                .loadByCode(command.getPromotionCode());

        then(mockedOrder).should(times(1)).applyPromotion(promotion);

        then(createOrderPort).should(times(1)).save(mockedOrder);
    }

    @Test
    public void should_fail_checkout_with_unavailable_items()
            throws ItemUnavailableException, PromotionCodeNotFoundException {

        final Set<Item> newItems = this.items.stream()
                .map(item -> new Item(item.getItemId(), item.getQuantity() * 2))
                .collect(Collectors.toSet());

        CheckoutCommand command = new CheckoutCommand(randomUUID().toString(), newItems, randomUUID().toString());

        final Set<InventoryItem> inventoryItems = getInventoryItems();

        given(loadInventoryPort.loadItemsById(mapToInventoryItemId()))
                .willReturn(inventoryItems);


        thrown.expect(ItemUnavailableException.class);

        checkoutService.checkout(command);

        then(updateInventoryPort).should(never()).update(inventoryItems);

        then(createOrderPort).should(never()).save(any(Order.class));

    }

    private void thenAssertThatInventoryItemHasBeenDecremented(Set<InventoryItem> inventoryItems) {
        final Set<OrderItem> orderItems = getOrderItems();

        for (InventoryItem item: inventoryItems) {
            final OrderItem orderItem = orderItems.stream().filter(oitem -> oitem.getId().getValue().equals(item.getId().getValue()))
                    .findFirst().get();

            assertThat(item.getStock()).isEqualTo(DEFAULT_STOCK - orderItem.getQuantity());
        }
    }

    private Set<InventoryItemId> mapToInventoryItemId() {
        return items.stream().map(item -> new InventoryItemId(item.getItemId())).collect(Collectors.toSet());
    }

    private Set<InventoryItem> getInventoryItems() {
        Set<InventoryItem> inventoryItems = new HashSet<>();

        for (Item item : items) {
            inventoryItems.add(new InventoryItem(new InventoryItemId(item.getItemId()),
                    new BigDecimal(150 / item.getQuantity()), DEFAULT_STOCK));
        }

        return inventoryItems;
    }

    private Set<OrderItem> getOrderItems() {
        Set<OrderItem> orderItems = new HashSet<>();

        for (Item item : items) {
            orderItems.add(OrderItem.with(new OrderItemId(item.getItemId()),
                    new BigDecimal(150 / item.getQuantity()), item.getQuantity()));
        }

        return orderItems;
    }

    private HashSet<Item> createItemsCommand() {

        final String item1 = randomUUID().toString();
        final String item2 = randomUUID().toString();
        final String item3 = randomUUID().toString();

        return new HashSet<>(Arrays.asList(
                new Item(item1, 6),
                new Item(item2, 3),
                new Item(item3, 2)
        ));
    }
}
