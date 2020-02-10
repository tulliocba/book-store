package com.github.tulliocba.bookstore.persistence.store;

import org.springframework.data.jpa.repository.JpaRepository;

interface OrderRepository extends JpaRepository<OrderEntity, Long> {}
