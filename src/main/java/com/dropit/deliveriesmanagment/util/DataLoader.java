package com.dropit.deliveriesmanagment.util;

import com.dropit.deliveriesmanagment.delivery.models.TimeSlot;
import com.dropit.deliveriesmanagment.delivery.repositories.TimeSlotRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Component
public class DataLoader implements ApplicationRunner {

    private static ObjectMapper objectMapper;

    private final TimeSlotRepository timeSlotRepository;


    @Autowired
    public DataLoader(TimeSlotRepository timeSlotRepository) {
        this.timeSlotRepository = timeSlotRepository;
        objectMapper = new ObjectMapper();
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        List<TimeSlot> availableTimeSlots = getAvailableTimeSlots();
        timeSlotRepository.saveAll(availableTimeSlots);

    }

    private List<TimeSlot> getAvailableTimeSlots() throws Exception {
        List<TimeSlot> timeSlots = fetchAvailableTimeSlots().get();
        List<LocalDate> forbiddenDates = fetchForbiddenDates().get();
        for (TimeSlot timeSlot : timeSlots) {
            for (LocalDate forbiddenDate : forbiddenDates) {
                if (compareDateToDateTime(timeSlot.getStartTime(), forbiddenDate) ||
                        compareDateToDateTime(timeSlot.getEndTime(), forbiddenDate)) {
                    timeSlots.remove(timeSlot);
                    break;
                }
            }
        }

        return timeSlots;
    }

    @Async
    Future<List<TimeSlot>> fetchAvailableTimeSlots() throws Exception {
        String file = "src/main/resources/static/AvailableTimeSlots.json";
        String json = readFileAsString(file);
        return CompletableFuture.completedFuture(Arrays.asList(objectMapper.readValue(json, TimeSlot[].class)));

    }

    @Async
    Future<List<LocalDate>> fetchForbiddenDates() throws IOException {

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://holidayapi.com/v1/holidays?pretty&key=87580778-658b-46c5-aebc-4f56f33d2005&country=IL&year=2021")
                .method("GET", null)
                .build();

        Response response = client.newCall(request).execute();
        JsonNode holidays = objectMapper.readTree(response.body().string()).path("holidays");

        return extractDatesFromJsonNode(holidays);
    }

    @NotNull
    private Future<List<LocalDate>> extractDatesFromJsonNode(JsonNode holidays) {
        List<LocalDate> forbiddenTimes = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (JsonNode holiday : holidays) {
            forbiddenTimes.add(LocalDate.parse(holiday.path("date").asText(), formatter));

        }

        return CompletableFuture.completedFuture(forbiddenTimes);
    }

    public String readFileAsString(String file) throws Exception {
        return new String(Files.readAllBytes(Paths.get(file)));
    }

    private boolean compareDateToDateTime(LocalDateTime dateTime, LocalDate date) {
        return date.getYear() == dateTime.getYear() &&
                date.getMonthValue() == dateTime.getMonthValue() &&
                date.getDayOfMonth() == dateTime.getDayOfMonth();

    }
}
