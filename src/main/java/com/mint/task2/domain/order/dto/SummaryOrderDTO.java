package com.mint.task2.domain.order.dto;



import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SummaryOrderDTO implements Serializable {


    private BigDecimal totalOrderAmount;


    private Integer totalOrder;

    private LocalDate orderDate;

}

