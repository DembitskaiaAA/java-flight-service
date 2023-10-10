package com.gridnine.testing.flightcontrol;

import com.gridnine.testing.flightcontrol.flightEnums.FlightStateTypes;
import com.gridnine.testing.flightcontrol.flightEnums.FlightTimeTypes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FlightServiceImpTest {
    FlightService flightService = new FlightServiceImp();

    LocalDateTime localDateTime = LocalDateTime.of(2023, 10, 10, 0, 0);
    List<Segment> segmentNormal = List.of(new Segment(localDateTime.minusHours(1), localDateTime.plusHours(3)));
    List<Segment> segmentsNormal = List.of(new Segment(localDateTime.plusHours(1), localDateTime.plusHours(3)),
            new Segment(localDateTime.plusHours(6), localDateTime.plusHours(9)));
    List<Segment> segmentsWrongArrivalTime = List.of(new Segment(localDateTime, localDateTime.plusHours(3)),
            new Segment(localDateTime.plusHours(6), localDateTime.minusHours(10)));
    List<Flight> flights = new ArrayList<>(List.of(new Flight(segmentNormal),
            new Flight(segmentsNormal),
            new Flight(segmentsWrongArrivalTime)));

    @Test
    @DisplayName("Filter Flights: Departure After Now, Transit Time > 2 hours")
    void testFilterFlightsByAllRulesAfterTransit() {
        List<Flight> result = flightService.filterFlightsByAllRules(flights, localDateTime, FlightTimeTypes.AFTER,
                FlightStateTypes.TRANSIT, 2);
        List<Flight> expectedResult = new ArrayList<>(List.of(new Flight(segmentsNormal)));

        assertEquals(expectedResult.size(), result.size());
        assertArrayEquals(expectedResult.toArray(), result.toArray());
    }

    @Test
    @DisplayName("Filter Flights: Departure Before Now, Transit Time > 2 hours")
    void testFilterFlightsByAllRulesBeforeTransit() {
        List<Flight> result = flightService.filterFlightsByAllRules(flights, localDateTime, FlightTimeTypes.BEFORE,
                FlightStateTypes.TRANSIT, 2);
        List<Flight> expectedResult = new ArrayList<>(List.of(new Flight(segmentNormal)));

        assertEquals(expectedResult.size(), result.size());
        assertArrayEquals(expectedResult.toArray(), result.toArray());
    }

    @Test
    @DisplayName("Filter Flights: Departure After Now, Flight Time > 5 hours")
    void testFilterFlightsByAllRulesAfterFlight() {
        List<Flight> result = flightService.filterFlightsByAllRules(flights, localDateTime, FlightTimeTypes.AFTER,
                FlightStateTypes.FLIGHT, 5);
        List<Flight> expectedResult = new ArrayList<>(List.of(new Flight(segmentsNormal)));

        assertEquals(expectedResult.size(), result.size());
        assertArrayEquals(expectedResult.toArray(), result.toArray());
    }

    @Test
    @DisplayName("Filter Flights: Departure Before Now, Flight Time > 3 hours")
    void testFilterFlightsByAllRulesBeforeFlight() {
        List<Flight> result = flightService.filterFlightsByAllRules(flights, localDateTime, FlightTimeTypes.BEFORE,
                FlightStateTypes.FLIGHT, 3);
        List<Flight> expectedResult = new ArrayList<>(List.of(new Flight(segmentNormal)));

        assertEquals(expectedResult.size(), result.size());
        assertArrayEquals(expectedResult.toArray(), result.toArray());
    }

    @Test
    @DisplayName("Filter Flights: Departure After Now")
    void testCheckDepartureTimeLaterCorrect() {
        List<Flight> result = flightService.checkDepartureTimeLaterOrBefore(flights, localDateTime, FlightTimeTypes.AFTER);
        List<Flight> expectedResult = new ArrayList<>(List.of(new Flight(segmentsNormal)));

        assertEquals(expectedResult.size(), result.size());
        assertArrayEquals(expectedResult.toArray(), result.toArray());
    }

    @Test
    @DisplayName("Filter Flights: Departure Before Now")
    void testCheckDepartureTimeBeforeCorrect() {
        List<Flight> result = flightService.checkDepartureTimeLaterOrBefore(flights, localDateTime, FlightTimeTypes.BEFORE);
        List<Flight> expectedResult = new ArrayList<>(List.of(new Flight(segmentNormal)));

        assertEquals(expectedResult.size(), result.size());
        assertArrayEquals(expectedResult.toArray(), result.toArray());
    }

    @Test
    @DisplayName("Filter Flights: Arrival Time After Departure Time")
    void testCheckArrivalTimeLaterDepartureTime() {
        List<Flight> result = flightService.checkArrivalTimeLaterDepartureTime(flights);
        List<Flight> expectedResult = new ArrayList<>(List.of(new Flight(segmentNormal), new Flight(segmentsNormal)));

        assertEquals(expectedResult.size(), result.size());
        assertArrayEquals(expectedResult.toArray(), result.toArray());
    }

    @Test
    @DisplayName("Filter Flights: Transit Time > 3 hours")
    void testCheckTransitTimeCorrect() {
        List<Flight> result = flightService.checkTransitFlightTimeForSumHours(flights, FlightStateTypes.TRANSIT, 3);
        List<Flight> expectedResult = new ArrayList<>(List.of(new Flight(segmentNormal), new Flight(segmentsNormal)));

        assertEquals(expectedResult.size(), result.size());
        assertArrayEquals(expectedResult.toArray(), result.toArray());
    }

    @Test
    @DisplayName("Filter Flights: Transit Time > 4 hours")
    void testCheckTransitTimeFourHours() {
        List<Flight> result = flightService.checkTransitFlightTimeForSumHours(flights, FlightStateTypes.TRANSIT, 4);
        List<Flight> expectedResult = new ArrayList<>(List.of(new Flight(segmentNormal)));

        assertEquals(expectedResult.size(), result.size());
        assertArrayEquals(expectedResult.toArray(), result.toArray());
    }

    @Test
    @DisplayName("Filter Flights: Flight Time > 2 hours")
    void testCheckFlightTimeCorrect() {
        List<Flight> result = flightService.checkTransitFlightTimeForSumHours(flights, FlightStateTypes.FLIGHT, 2);
        List<Flight> expectedResult = new ArrayList<>(List.of(new Flight(segmentNormal), new Flight(segmentsNormal)));

        assertEquals(expectedResult.size(), result.size());
        assertArrayEquals(expectedResult.toArray(), result.toArray());
    }

    @Test
    @DisplayName("Filter Flights: Flight Time > 5 hours")
    void testCheckFlightTimeFiveHours() {
        List<Flight> result = flightService.checkTransitFlightTimeForSumHours(flights, FlightStateTypes.FLIGHT, 5);
        List<Flight> expectedResult = new ArrayList<>(List.of(new Flight(segmentsNormal)));

        assertEquals(expectedResult.size(), result.size());
        assertArrayEquals(expectedResult.toArray(), result.toArray());
    }
}