package com.github.tulliocba.persistence.store;

import com.github.tulliocba.bookstore.store.application.service.ItemUnavailableException;
import com.github.tulliocba.bookstore.store.domain.InventoryItem;
import com.github.tulliocba.bookstore.store.domain.InventoryItem.InventoryItemId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@DataJpaTest
@Import({InventoryPersistenceAdapter.class})
public class InventoryPersistenceAdapterTest {

    @Autowired
    private InventoryPersistenceAdapter adapter;

    @Test
    @Sql("classpath:com.github.tulliocba.persistence.store/InventoryPersistenceAdapterTest.sql")
    public void updateInventory() throws ItemUnavailableException {
        final Set<InventoryItem> inventoryItems = adapter.loadItemsById(
                new HashSet<>(Arrays.asList(new InventoryItemId(1L))));

        for (InventoryItem item: inventoryItems) item.decrementStock(2);

        adapter.update(inventoryItems);

        final Set<InventoryItem> itemsUpdated = adapter.loadItemsById(
                new HashSet<>(Arrays.asList(new InventoryItemId(1L))));

        for (InventoryItem item: itemsUpdated) Assertions.assertThat(item.getStock()).isEqualTo(8);


    }
}
