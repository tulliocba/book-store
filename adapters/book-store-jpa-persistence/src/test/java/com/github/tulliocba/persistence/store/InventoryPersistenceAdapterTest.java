package com.github.tulliocba.persistence.store;

import com.github.tulliocba.bookstore.store.application.service.ItemUnavailableException;
import com.github.tulliocba.bookstore.store.domain.InventoryItem;
import com.github.tulliocba.bookstore.store.domain.InventoryItem.InventoryItemId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({InventoryPersistenceAdapter.class})
public class InventoryPersistenceAdapterTest {

    @Autowired
    private InventoryPersistenceAdapter adapter;

    @Test
    @Sql("classpath:com.github.tulliocba.persistence.store/InventoryPersistenceAdapterTest.sql")
    public void should_update_inventory_item() throws ItemUnavailableException {
        final Set<InventoryItem> inventoryItems = adapter.loadItemsById(
                new HashSet<>(Arrays.asList(new InventoryItemId(1L))));

        for (InventoryItem item: inventoryItems) item.decrementStock(2);

        adapter.update(inventoryItems);

        final Set<InventoryItem> itemsUpdated = adapter.loadItemsById(
                new HashSet<>(Arrays.asList(new InventoryItemId(1L))));

        for (InventoryItem item: itemsUpdated) assertThat(item.getStock()).isEqualTo(8);
    }

    @Test
    @Sql("classpath:com.github.tulliocba.persistence.store/InventoryPersistenceAdapterTest.sql")
    void should_load_inventory_item_by_id() {
        final Set<InventoryItem> inventoryItems = adapter
                .loadItemsById(new HashSet<>(Arrays.asList(new InventoryItemId(1L))));

        for (InventoryItem item : inventoryItems) assertThat(item.getId()).isEqualTo(new InventoryItemId(1L));
    }
}
