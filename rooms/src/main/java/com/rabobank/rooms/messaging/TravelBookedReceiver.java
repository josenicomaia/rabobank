package com.rabobank.rooms.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabobank.rooms.messaging.external.TravelBookedEvent;
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

    @RabbitListener(queues = "rooms-queue")
    public void receiveMessage(String event) throws JsonProcessingException {
        TravelBookedEvent travelBookedEvent = objectMapper.readValue(event, TravelBookedEvent.class);
        System.out.println("Received <" + travelBookedEvent + ">");

        RoomsBookedEvent roomsBookedEvent = new RoomsBookedEvent(travelBookedEvent.tripId(), travelBookedEvent.userId());
        sendEvent(roomsBookedEvent);
    }

    private void sendEvent(RoomsBookedEvent roomsBookedEvent) throws JsonProcessingException {
        System.out.println("Sending <" + roomsBookedEvent + ">");
        rabbitTemplate.convertAndSend("rooms.booked", objectMapper.writeValueAsString(roomsBookedEvent));
    }
}
