package com.mint.task2.domain.order;


import com.mint.task2.domain.kafka.KafkaConsumerService;
import com.mint.task2.domain.order.dto.OrderDTO;
import com.mint.task2.domain.order.dto.SummaryOrderDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepo orderRepo;
    private final KafkaConsumerService kafkaConsumerService;
    private final ModelMapper modelMapper = new ModelMapper();

    @Bean
    public void setModelMapper() {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }


    @Override
    public List<SummaryOrderDTO> getOrderHistory(LocalDate fromDate, LocalDate toDate) {
        List<OrderEntity> orderEntities = orderRepo.findByOrderDateBetween(fromDate, toDate);

        Map<LocalDate, SummaryOrderDTO> check = new HashMap<>();

        for (OrderEntity dto : orderEntities) {
            if (check.containsKey(dto.getOrderDate())) {
                SummaryOrderDTO orderDTO = check.get(dto.getOrderDate());
                SummaryOrderDTO newOrder = new SummaryOrderDTO();
                newOrder.setOrderDate(dto.getOrderDate());
                newOrder.setTotalOrder(orderDTO.getTotalOrder() + 1);
                newOrder.setTotalOrderAmount(orderDTO.getTotalOrderAmount().add(dto.getPrice()));
                check.put(dto.getOrderDate(), newOrder);

            } else {
                check.put(dto.getOrderDate(), SummaryOrderDTO.builder().totalOrder(1).orderDate(dto.getOrderDate()).totalOrderAmount(dto.getPrice()).build());
            }
        }
        return check.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList());
    }
}
