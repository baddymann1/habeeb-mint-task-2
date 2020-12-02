package com.mint.task2.domain.order;


import com.mint.task2.domain.kafka.KafkaService;
import com.mint.task2.domain.order.dto.AddOrderDTO;
import com.mint.task2.domain.order.dto.OrderDTO;
import com.mint.task2.domain.order.dto.UpdateOrderDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepo orderRepo;
    private final KafkaService kafkaService;
    private final ModelMapper modelMapper = new ModelMapper();

    @Bean
    public void setModelMapper() {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }


    @Override
    public OrderDTO getOrderHistory(LocalDate fromDate, LocalDate toDate) {
        return null;
    }
}
