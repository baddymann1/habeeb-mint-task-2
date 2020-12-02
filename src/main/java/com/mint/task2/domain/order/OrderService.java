package com.mint.task2.domain.order;


import com.mint.task2.domain.order.dto.AddOrderDTO;
import com.mint.task2.domain.order.dto.OrderDTO;
import com.mint.task2.domain.order.dto.UpdateOrderDTO;

import java.time.LocalDate;

public interface OrderService {
    OrderDTO getOrderHistory(LocalDate fromDate, LocalDate toDate);

}
