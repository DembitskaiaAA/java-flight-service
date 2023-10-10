package com.gridnine.testing.flightcontrol;

import com.gridnine.testing.flightcontrol.flightEnums.FlightStateTypes;
import com.gridnine.testing.flightcontrol.flightEnums.FlightTimeTypes;

import java.time.LocalDateTime;
import java.util.List;

public interface FlightService {

    List<Flight> filterFlightsByAllRules(List<Flight> flights,
                                         LocalDateTime requiredDate,
                                         FlightTimeTypes flightTimeTypes,
                                         FlightStateTypes flightStateTypes,
                                         int hours);

    List<Flight> checkDepartureTimeLaterOrBefore(List<Flight> flights, LocalDateTime requiredDate, FlightTimeTypes flightTimeTypes);

    List<Flight> checkArrivalTimeLaterDepartureTime(List<Flight> flights);

    List<Flight> checkTransitFlightTimeForSumHours(List<Flight> flights, FlightStateTypes flightStateTypes, int hours);
}
