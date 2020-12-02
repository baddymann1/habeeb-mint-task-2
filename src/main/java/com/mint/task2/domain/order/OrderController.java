package com.mint.task2.domain.order;


import com.mint.task2.config.ApiResponseBase;
import com.mint.task2.domain.order.dto.AddOrderDTO;
import com.mint.task2.domain.order.dto.OrderDTO;
import com.mint.task2.domain.order.dto.SummaryOrderDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequestMapping("/api/v1/order")
@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/summary-report")
    public ResponseEntity<ApiResponseBase<List<SummaryOrderDTO>>> getProductHistory(
            @RequestParam("fromDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam("toDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        ApiResponseBase<List<SummaryOrderDTO>> pagedApiResponseBase = new ApiResponseBase<>();
        pagedApiResponseBase.setResponse(orderService.getOrderHistory(fromDate, toDate));
        return new ResponseEntity<>(pagedApiResponseBase, HttpStatus.OK);
    }
}
