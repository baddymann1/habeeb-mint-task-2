package com.mint.task2.domain.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepo extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findByOrderDateBetween(LocalDate fromDate, LocalDate toDate);
}
