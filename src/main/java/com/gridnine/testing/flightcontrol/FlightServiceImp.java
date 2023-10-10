package com.gridnine.testing.flightcontrol;

import com.gridnine.testing.flightcontrol.flightEnums.FlightStateTypes;
import com.gridnine.testing.flightcontrol.flightEnums.FlightTimeTypes;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FlightServiceImp implements FlightService {

    // Метод фильтрации полетов по нескольким правилам
    @Override
    public List<Flight> filterFlightsByAllRules(List<Flight> flights,
                                                LocalDateTime requiredDate,
                                                FlightTimeTypes flightTimeTypes,
                                                FlightStateTypes flightStateTypes,
                                                int hours) {
        // Фильтрация по времени отправления
        List<Flight> flightsDepartureTimeFilter = checkDepartureTimeLaterOrBefore(flights, requiredDate, flightTimeTypes);
        // Фильтрация по времени прибытия и отправления
        List<Flight> flightsArrivalTimeFilter = checkArrivalTimeLaterDepartureTime(flights);
        // Фильтрация по времени транзита или полета
        List<Flight> flightsTransitTimeFilter = checkTransitFlightTimeForSumHours(flights, flightStateTypes, hours);

        return flights.stream()
                .filter(flightsDepartureTimeFilter::contains)
                .filter(flightsArrivalTimeFilter::contains)
                .filter(flightsTransitTimeFilter::contains)
                .distinct()
                .collect(Collectors.toList());
    }

    // Метод фильтрации полетов по времени отправления
    @Override
    public List<Flight> checkDepartureTimeLaterOrBefore(List<Flight> flights,
                                                        LocalDateTime requiredDate,
                                                        FlightTimeTypes flightTimeTypes) {
        List<Flight> result = new ArrayList<>(flights.size());
        switch (flightTimeTypes) {
            case AFTER:
                // Фильтрация полетов с отправлением после заданной даты
                flights.stream()
                        .filter(f -> f.getSegments().get(0).getDepartureDate().isAfter(requiredDate))
                        .collect(Collectors.toCollection(() -> result));
                break;

            case BEFORE:
                // Фильтрация полетов с отправлением до заданной даты
                flights.stream()
                        .filter(f -> f.getSegments().stream().anyMatch(s -> s.getDepartureDate().isBefore(requiredDate)))
                        .collect(Collectors.toCollection(() -> result));
                break;
        }
        return result;
    }

    // Метод фильтрации полетов по времени прибытия и отправления
    @Override
    public List<Flight> checkArrivalTimeLaterDepartureTime(List<Flight> flights) {
        return removeFlightsDepartureTimeLaterArrivalTime(flights);
    }

    // Фильтрация полетов, где время прибытия на всех сегментах идет после времени отправления
    private List<Flight> removeFlightsDepartureTimeLaterArrivalTime(List<Flight> flights) {
        return flights.stream()
                .filter(f -> f.getSegments().stream().allMatch(s -> s.getArrivalDate().isAfter(s.getDepartureDate())))
                .collect(Collectors.toList());
    }

    // Метод фильтрации полетов по времени транзита и полета
    @Override
    public List<Flight> checkTransitFlightTimeForSumHours(List<Flight> flights,
                                                          FlightStateTypes flightStateTypes,
                                                          int hours) {
        List<Flight> result = new ArrayList<>(flights.size());
        // Коллекция, в которой исключены рейсы с прилетом раньше вылета
        List<Flight> flightsCorrectDepartureTimeArrivalTime = removeFlightsDepartureTimeLaterArrivalTime(flights);
        switch (flightStateTypes) {
            case TRANSIT:
                // Фильтрация полетов по времени транзита
                result = checkTransitTimeForSumHours(flightsCorrectDepartureTimeArrivalTime, hours);
                break;

            case FLIGHT:
                // Фильтрация полетов по времени транзита
                result = checkFlightTimeForSumHours(flightsCorrectDepartureTimeArrivalTime, hours);
                break;
        }
        return result;
    }

    // Метод фильтрации полетов по количеству часов транзита
    private List<Flight> checkTransitTimeForSumHours(List<Flight> flights, int hours) {
        List<Flight> result = new ArrayList<>();

        for (Flight flight : flights) {
            List<Segment> segments = flight.getSegments();

            if (hasSufficientSegments(segments, hours)) {
                result.add(flight);
            }
        }
        return result;
    }

    // Метод проверки на количество сегментов
    private boolean hasSufficientSegments(List<Segment> segments, int hours) {
        if (segments.size() < 2) {
            return true;
        }
        long totalTransitHours = calculateTotalTransitTime(segments);
        return totalTransitHours >= hours;
    }

    // Метод вычисления общего времени транзита для сегментов
    private long calculateTotalTransitTime(List<Segment> segments) {
        long totalTransitHours = 0L;

        for (int i = 1; i < segments.size(); i++) {
            Duration duration = Duration.between(segments.get(i - 1).getArrivalDate(), segments.get(i).getDepartureDate());
            totalTransitHours += duration.toHours();
        }
        return totalTransitHours;
    }

    // Метод фильтрации полетов по времени полета
    private List<Flight> checkFlightTimeForSumHours(List<Flight> flights, int hours) {
        List<Flight> result = new ArrayList<>();

        for (Flight flight : flights) {
            List<Segment> segments = flight.getSegments();

            if (calculateTotalFlightTime(segments) >= hours) {
                result.add(flight);
            }
        }
        return result;
    }

    // Метод вычисления общего времени полета для сегментов
    private long calculateTotalFlightTime(List<Segment> segments) {
        long totalTransitHours = 0L;
        if (segments.size() >= 2) {
            for (int i = 0; i < segments.size(); i++) {
                Duration duration = Duration.between(segments.get(i).getDepartureDate(), segments.get(i).getArrivalDate());
                totalTransitHours += duration.toHours();
            }
        } else {
            Duration duration = Duration.between(segments.get(0).getDepartureDate(), segments.get(0).getArrivalDate());
            totalTransitHours = duration.toHours();
        }
        return totalTransitHours;
    }
}
