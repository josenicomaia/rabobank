package com.rabobank.cars.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabobank.cars.messaging.external.TravelBookedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class TravelBookedReceiver {
    private final ObjectMapper objectMapper;
    private final RabbitTemplate rabbitTemplate;

    public TravelBookedReceiver(ObjectMapper objectMapper, RabbitTemplate rabbitTemplate) {
        this.objectMapper = objectMapper;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = "cars-queue")
    public void receiveMessage(String event) throws JsonProcessingException {
        TravelBookedEvent travelBookedEvent = objectMapper.readValue(event, TravelBookedEvent.class);
        System.out.println("Received <" + travelBookedEvent + ">");

        CarBookedEvent carBookedEvent = new CarBookedEvent(travelBookedEvent.tripId(), travelBookedEvent.userId());
        sendEvent(carBookedEvent);
    }

    private void sendEvent(CarBookedEvent carBookedEvent) throws JsonProcessingException {
        System.out.println("Sending <" + carBookedEvent + ">");
        rabbitTemplate.convertAndSend("cars.booked", objectMapper.writeValueAsString(carBookedEvent));
    }
}
