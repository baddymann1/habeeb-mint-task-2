package com.mint.task2.domain.order;


import com.mint.task2.config.ApiResponseBase;
import com.mint.task2.domain.order.dto.AddOrderDTO;
import com.mint.task2.domain.order.dto.OrderDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RequestMapping("/api/v1/order")
@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/summary-report/{fromDate}/{toDate}")
    public ResponseEntity<ApiResponseBase<OrderDTO>> getProductHistory(@PathVariable("fromDate") LocalDate fromDate, @PathVariable("toDate") LocalDate toDate) {
        ApiResponseBase<OrderDTO> pagedApiResponseBase = new ApiResponseBase<>();
        pagedApiResponseBase.setResponse(orderService.getOrderHistory(fromDate, toDate));
        return new ResponseEntity<>(pagedApiResponseBase, HttpStatus.OK);
    }
}
