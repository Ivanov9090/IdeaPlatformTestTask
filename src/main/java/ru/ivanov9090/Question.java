package ru.ivanov9090;

/*
 * Класс скрывает от пользователя реализацию ответа на вопросы.
 * Для каждого вопроса существет отдельный метод.
 * Парсинг выведен в отдельный метод.
 */

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Question {
    private static final String QUESTION_1 = "Минимальное время полета между городами Владивосток и Тель-Авив для авиаперевозчика %s равно %d часов %d минут.\n";
    private static final String QUESTION_2 = "Разница между средней ценой и медианой для полета между городами Владивосток и Тель-Авив равна %.0f.\n";

    /*
     * Вопрос 1: Минимальное время полета между городами Владивосток и Тель-Авив для каждого авиаперевозчика
     */
    public static void question1(String fileName, String origin, String destination) {
        // Получаем список билетов
        List<Ticket> tickets = parseTickets(fileName);

        // Составляем множество перевозчиков
        Set<String> carriers = new HashSet<>();
        for (Ticket ticket : tickets) {
            carriers.add(ticket.getCarrier());
        }

        // Обрабатываем наш список
        for (String carrier : carriers) {
            tickets.stream()
                    .filter(ticket -> ticket.getCarrier().equals(carrier))
                    .filter(ticket -> ticket.getOriginName().equals(origin))
                    .filter(ticket -> ticket.getDestinationName().equals(destination))
                    .min((x, y) -> x.getFlightTime() - y.getFlightTime())
                    .ifPresent(ticket -> System.out.printf(QUESTION_1, ticket.getCarrier(), ticket.getFlightTime() / 60, ticket.getFlightTime() % 60));
        }
    }

    /*
     * Вопрос 2: Разница между средней ценой и медианой для полета между городами Владивосток и Тель-Авив
     */
    public static void question2(String fileName, String origin, String destination) {
        // Получаем список билетов
        List<Ticket> tickets = parseTickets(fileName);

        // Вычисляем седнюю цену
        double avaragePrice = tickets.stream()
                .filter(ticket -> ticket.getOriginName().equals(origin))
                .filter(ticket -> ticket.getDestinationName().equals(destination))
                .mapToDouble(Ticket::getPrice)
                .average()
                .getAsDouble();

        // Вычисляем медиану
        double median;
        int[] sortedPrice = tickets.stream()
                .filter(ticket -> ticket.getOriginName().equals(origin))
                .filter(ticket -> ticket.getDestinationName().equals(destination))
                .mapToInt(Ticket::getPrice)
                .sorted()
                .toArray();
        if (sortedPrice.length % 2 == 0) {
            median = (sortedPrice[sortedPrice.length / 2 - 1] + sortedPrice[sortedPrice.length / 2]) / 2;
        } else median = sortedPrice[sortedPrice.length / 2];

        // Выводим результат
        System.out.printf(QUESTION_2, Math.abs(avaragePrice - median));
    }

    /*
     * Парсинг файла
     */
    private static List<Ticket> parseTickets(String fileName) {
        // Читаем файл из той же директории
        String workingDirectory = System.getProperty("user.dir");
        String filePath = workingDirectory + File.separator + fileName;

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String jsonContent = new String(Files.readAllBytes(Paths.get(filePath)));

            // Убираем BOM, если он примутствует
            if (jsonContent.startsWith("\uFEFF")) {
                jsonContent = jsonContent.substring(1);
            }

            return objectMapper.readValue(jsonContent, TicketList.class).getTickets();
        } catch (IOException e) {
            System.out.println("Ошибка при чтении tickets.json");
            throw new RuntimeException(e);
        }
    }
}
