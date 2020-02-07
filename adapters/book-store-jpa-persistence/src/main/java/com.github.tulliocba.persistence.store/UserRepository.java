package com.github.tulliocba.persistence.store;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserRepository, Long> {
}
