package com.github.tulliocba.bookstore.store.application.service;

import com.github.tulliocba.bookstore.store.application.port.in.CheckoutUseCase.CheckoutCommand;
import com.github.tulliocba.bookstore.store.application.port.out.CreateOrderPort;
import com.github.tulliocba.bookstore.store.application.port.out.LoadPromotionPort;
import com.github.tulliocba.bookstore.store.application.port.out.UpdateInventoryPort;
import com.github.tulliocba.bookstore.store.domain.Customer.CustomerId;
import com.github.tulliocba.bookstore.store.domain.Order;
import com.github.tulliocba.bookstore.store.domain.OrderItem;
import com.github.tulliocba.bookstore.store.domain.OrderItem.OrderItemId;
import com.github.tulliocba.bookstore.store.domain.Promotion;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.github.tulliocba.bookstore.store.application.port.in.CheckoutUseCase.Item;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CheckoutService.class)
public class CheckoutServiceTest {

    private LoadPromotionPort loadPromotionPort;
    private CreateOrderPort createOrderPort;
    private UpdateInventoryPort updateInventoryPort;
    private CheckoutService checkoutService;
    private Set<Item> items;

    @Before
    public void setUp() {
        loadPromotionPort = mock(LoadPromotionPort.class);
        createOrderPort = mock(CreateOrderPort.class);
        updateInventoryPort = mock(UpdateInventoryPort.class);
        checkoutService = new CheckoutService(updateInventoryPort, loadPromotionPort, createOrderPort);

        items = createItemsCommand();
    }

    @Test
    public void should_succeeds_checkout_without_promotion_code() throws Exception {

        CheckoutCommand command = new CheckoutCommand(randomUUID().toString(), items, null);

        final Set<OrderItem> decrementedItems = getOrderItems();

        given(updateInventoryPort.decrementInventory(any(Set.class)))
                .willReturn(decrementedItems);

        checkoutService.checkout(command);

        then(updateInventoryPort).should(times(1)).decrementInventory(decrementedItems);

        then(loadPromotionPort).should(times(0))
                .loadByCode(command.getPromotionCode());

        final Order mockedOrder = mock(Order.class);

        whenNew(Order.class)
                .withArguments(decrementedItems, new CustomerId(command.getCustomerId()))
                .thenReturn(mockedOrder);

        assertThat(mockedOrder).isNotNull();

        then(mockedOrder).should(times(0)).applyPromotion(any(Promotion.class));

        then(createOrderPort).should(times(1)).save(any(Order.class));
    }

    @Test
    public void should_succeeds_checkout_with_promotion_code() throws Exception {

        final String promotionCode = randomUUID().toString();

        CheckoutCommand command = new CheckoutCommand(randomUUID().toString(), items, promotionCode);

        final Set<OrderItem> decrementedItems = getOrderItems();

        given(updateInventoryPort.decrementInventory(any(Set.class)))
                .willReturn(decrementedItems);

        final Promotion promotion = new Promotion(promotionCode, 10, LocalDateTime.now().plusDays(1));

        given(loadPromotionPort.loadByCode(command.getPromotionCode()))
                .willReturn(promotion);

        final Order mockedOrder = mock(Order.class);

        whenNew(Order.class)
                .withArguments(decrementedItems, new CustomerId(command.getCustomerId()))
                .thenReturn(mockedOrder);

        checkoutService.checkout(command);

        then(updateInventoryPort).should(times(1))
                .decrementInventory(decrementedItems);

        then(loadPromotionPort).should(times(1))
                .loadByCode(command.getPromotionCode());

        then(mockedOrder).should().applyPromotion(promotion);

        then(createOrderPort).should(times(1)).save(mockedOrder);
    }

    private Set<OrderItem> getOrderItems() {
        Set<OrderItem> orderItems = new HashSet<>();

        for (Item item : items) {
            orderItems.add(OrderItem.with(new OrderItemId(item.getItemId()),
                    new BigDecimal(150 / item.getQuantity()),
                    item.getQuantity()));
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
