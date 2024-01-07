package com.rabobank.trips.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabobank.trips.TravelBookedEvent;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trips")
public class TripsController {
    private final Logger logger = LoggerFactory.getLogger(TripsController.class);
    private final ObjectMapper objectMapper;
    private final RabbitTemplate rabbitTemplate;

    public TripsController(ObjectMapper objectMapper, RabbitTemplate rabbitTemplate) {
        this.objectMapper = objectMapper;
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping
    public ResponseEntity<?> bookTrip(@Valid @RequestBody TripBookingRequest request) {
        try {
            sendEvent(new TravelBookedEvent(request.tripId(), request.userId()));

            return ResponseEntity.ok().build();
        } catch (JsonProcessingException e) {
            logger.error("Error while sending event", e);

            return ResponseEntity.status(500).build();
        }
    }

    private void sendEvent(TravelBookedEvent travelBookedEvent) throws JsonProcessingException {
        System.out.println("Sending <" + travelBookedEvent + ">");
        rabbitTemplate.convertAndSend("exchange", "trips.booked", objectMapper.writeValueAsString(travelBookedEvent));
    }

}
