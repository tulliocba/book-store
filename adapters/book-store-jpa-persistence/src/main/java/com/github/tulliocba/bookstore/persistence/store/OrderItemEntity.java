package com.github.tulliocba.bookstore.persistence.store;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "order_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class OrderItemEntity {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    @OneToOne
    @JoinColumn(name = "item_id")
    private InventoryItemEntity inventoryItem;

    private int quantity;

    public OrderItemEntity(OrderEntity order, InventoryItemEntity item, int quantity) {
        this.order = order;
        this.inventoryItem = item;
        this.quantity = quantity;
    }
}
