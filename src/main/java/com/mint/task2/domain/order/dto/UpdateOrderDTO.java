package com.mint.task2.domain.order.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
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
public class UpdateOrderDTO implements Serializable {

    private Long productId;

    private Long customerId;

    @NotNull(message = "quantity cannot be null")
    private Integer quantity;

    @NotNull(message = "price cannot be null")
    private BigDecimal price;

    private BigDecimal discount;



    private LocalDate orderDate;
    private LocalTime orderTime;
}
