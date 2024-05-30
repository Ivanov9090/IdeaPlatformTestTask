package ru.ivanov9090;

/*
 * Класс, в который происходит парсинг билетов
 * Для всех переменных присутствуют геттеры (если потребуется выводить другую информацию).
 * Сеттеры отсутствуют, т.к. программа не предусматривает изменение данных и записи их куда-либо.
 */

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Ticket {
    private String origin;
    private String originName;
    private String destination;
    private String destinationName;
    private LocalDateTime departureDateTime;
    private LocalDateTime arrivalDateTime;
    private String carrier;
    private int stops;
    private int price;

    // Вспомогательная переменная, которой нет в билете, но она может быть вычислена на основе других
    private int flightTime;

    public Ticket(@JsonProperty("origin") String origin,
                  @JsonProperty("origin_name") String originName,
                  @JsonProperty("destination") String destination,
                  @JsonProperty("destination_name") String destinationName,
                  @JsonProperty("departure_date") String departureDate,
                  @JsonProperty("departure_time") String departureTime,
                  @JsonProperty("arrival_date") String arrivalDate,
                  @JsonProperty("arrival_time") String arrivalTime,
                  @JsonProperty("carrier") String carrier,
                  @JsonProperty("stops") int stops,
                  @JsonProperty("price") int price) {
        this.origin = origin;
        this.originName = originName;
        this.destination = destination;
        this.destinationName = destinationName;

        // Переведем некоторые значения в LocalDateTime
        // Предусмотрим отсутствие 0 перед однозначным числом в дате
        String[] dateParts = departureDate.split("\\.");
        int year = Integer.parseInt(dateParts[2]);
        int month = Integer.parseInt(dateParts[1]);
        int day = Integer.parseInt(dateParts[0]);
        String[] timeParts = departureTime.split(":");
        int hours = Integer.parseInt(timeParts[0]);
        int minutes = Integer.parseInt(timeParts[1]);
        this.departureDateTime = LocalDateTime.of(2000 + year, month, day, hours, minutes);

        dateParts = arrivalDate.split("\\.");
        year = Integer.parseInt(dateParts[2]);
        month = Integer.parseInt(dateParts[1]);
        day = Integer.parseInt(dateParts[0]);
        timeParts = arrivalTime.split(":");
        hours = Integer.parseInt(timeParts[0]);
        minutes = Integer.parseInt(timeParts[1]);
        this.arrivalDateTime = LocalDateTime.of(2000 + year, month, day, hours, minutes);

        //Посчитаем время в полете в минутах (т.к. время указано с точностью до минуты)
        this.flightTime = (int) this.departureDateTime.until(arrivalDateTime, ChronoUnit.MINUTES);

        this.carrier = carrier;
        this.stops = stops;
        this.price = price;
    }

    public String getOrigin() {
        return origin;
    }

    public String getOriginName() {
        return originName;
    }

    public String getDestination() {
        return destination;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public LocalDateTime getDepartureDateTime() {
        return departureDateTime;
    }

    public LocalDateTime getArrivalDateTime() {
        return arrivalDateTime;
    }

    public String getCarrier() {
        return carrier;
    }

    public int getStops() {
        return stops;
    }

    public int getPrice() {
        return price;
    }

    public int getFlightTime() {
        return flightTime;
    }
}
