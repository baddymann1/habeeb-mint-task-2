package com.mint.task2.domain.order.dto;


import com.mint.task2.domain.customer.dto.CustomerDTO;
import com.mint.task2.domain.types.OrderStatus;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO implements Serializable {

    private  Long id;
    private Long productId;

    private Long customerId;

    private CustomerDTO customer;


    private Integer quantity;


    private BigDecimal price;

    private BigDecimal discount;


    private OrderStatus status;

    private LocalDate orderDate;

    private LocalTime orderTime;
}

