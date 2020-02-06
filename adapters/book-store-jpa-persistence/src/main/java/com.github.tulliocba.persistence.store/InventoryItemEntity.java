package com.github.tulliocba.persistence.store;

import com.github.tulliocba.bookstore.store.domain.InventoryItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "inventory_item")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal price;

    private Integer stock;

    private Long detailId;

    private InventoryItemEntity(Long id, BigDecimal price, int stock) {
        this.id = id;
        this.price = price;
        this.stock = stock;
    }

    public static InventoryItemEntity toEntity(InventoryItem item) {
        return new InventoryItemEntity(item.getId().getValue(), item.getPrice(), item.getStock());
    }


}
