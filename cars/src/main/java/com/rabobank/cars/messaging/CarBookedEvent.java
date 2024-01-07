package com.rabobank.cars.messaging;

public record CarBookedEvent(Long tripId, Long userId) {
}
