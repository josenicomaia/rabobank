package com.rabobank.flights.messaging;

public record FlightBookedEvent(Long tripId, Long userId) {
}
