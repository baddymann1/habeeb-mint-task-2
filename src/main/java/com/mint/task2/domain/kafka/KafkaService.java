package com.mint.task2.domain.kafka;

import com.mint.task2.domain.order.dto.OrderDTO;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {
    private final String TOPIC = "Kafka_Task1";



    @KafkaListener(topics = TOPIC, groupId = "order_group", containerFactory = "kafkaListenerContainerFactory")
    public void consumeOrder(OrderDTO orderDTO){
        System.out.println("Consumed Order " + orderDTO);
    }
}
