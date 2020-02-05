package com.github.tulliocba.persistence.store;

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

@DataJpaTest
@Import({InventoryPersistenceAdapter.class})
public class InventoryPersistenceAdapterTest {

    @Autowired
    private InventoryPersistenceAdapter adapter;

    @Test
    @Sql("InventoryPersistenceAdapterTest.sql")
    public void updateInventory() {
        final Set<InventoryItem> inventoryItems = adapter.loadItemsById(
                new HashSet<>(Arrays.asList(
                        new InventoryItemId("3287dedf-b1b5-46a6-b285-977bd39eb3a8"))));

        System.out.println(inventoryItems);


    }
}
