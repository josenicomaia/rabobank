package com.rabobank.flights.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabobank.flights.messaging.external.TravelBookedEvent;
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

    @RabbitListener(queues = "flights-queue")
    public void receiveMessage(String event) throws JsonProcessingException {
        TravelBookedEvent travelBookedEvent = objectMapper.readValue(event, TravelBookedEvent.class);
        System.out.println("Received <" + travelBookedEvent + ">");

        FlightBookedEvent flightBookedEvent = new FlightBookedEvent(travelBookedEvent.tripId(), travelBookedEvent.userId());
        sendEvent(flightBookedEvent);
    }

    private void sendEvent(FlightBookedEvent flightBookedEvent) throws JsonProcessingException {
        System.out.println("Sending <" + flightBookedEvent + ">");
        rabbitTemplate.convertAndSend("flights.booked", objectMapper.writeValueAsString(flightBookedEvent));
    }
}
