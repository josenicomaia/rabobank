package com.rabobank.trips.http;

import jakarta.validation.constraints.NotNull;

public record TripBookingRequest(
        @NotNull Long tripId,
        @NotNull Long userId) {
}
