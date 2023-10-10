package com.gridnine.testing.flightcontrol;

import com.gridnine.testing.flightcontrol.flightEnums.FlightStateTypes;
import com.gridnine.testing.flightcontrol.flightEnums.FlightTimeTypes;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        FlightService flightService = new FlightServiceImp();
        List<Flight> flights = FlightBuilder.createFlights();

        System.out.println("List of flights without filters:");
        printFlights(flights);

        System.out.println("List of flights with the filter: departure after the current moment in time:");
        printFlights(flightService.checkDepartureTimeLaterOrBefore(flights, LocalDateTime.now(), FlightTimeTypes.AFTER));

        System.out.println("List of flights with the filter: arrival time should be later than departure time:");
        printFlights(flightService.checkArrivalTimeLaterDepartureTime(flights));

        System.out.println("List of flights with the filter: time between flights should be more than two hours:");
        printFlights(flightService.checkTransitFlightTimeForSumHours(flights, FlightStateTypes.TRANSIT, 2));

        System.out.println("List of flights with all the above filters applied:");
        printFlights(flightService.filterFlightsByAllRules(flights, LocalDateTime.now(),
                FlightTimeTypes.AFTER, FlightStateTypes.TRANSIT, 2));
    }

    /**
     * Выводит в консоль список полётов.
     */
    private static void printFlights(List<Flight> flights) {
        int i = 0;
        for (Flight flight : flights) {
            System.out.println(++i + ". " + flight);
        }
        System.out.println("\n");
    }
}
