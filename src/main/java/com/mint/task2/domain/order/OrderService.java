package com.mint.task2.domain.order;


import com.mint.task2.domain.order.dto.AddOrderDTO;
import com.mint.task2.domain.order.dto.OrderDTO;
import com.mint.task2.domain.order.dto.SummaryOrderDTO;
import com.mint.task2.domain.order.dto.UpdateOrderDTO;

import java.time.LocalDate;
import java.util.List;

public interface OrderService {
    List<SummaryOrderDTO> getOrderHistory(LocalDate fromDate, LocalDate toDate);

}
