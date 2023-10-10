package com.gridnine.testing.flightcontrol;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FlightBuilderTest {

    @Test
    void createFlights() {
        List<Flight> flights = FlightBuilder.createFlights();
        assertEquals(6, flights.size());
    }
}