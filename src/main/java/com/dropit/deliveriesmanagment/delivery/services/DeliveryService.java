package com.dropit.deliveriesmanagment.delivery.services;


import com.dropit.deliveriesmanagment.delivery.dto.*;
import com.dropit.deliveriesmanagment.delivery.enums.Status;
import com.dropit.deliveriesmanagment.delivery.models.Delivery;
import com.dropit.deliveriesmanagment.delivery.models.TimeSlot;
import com.dropit.deliveriesmanagment.delivery.repositories.DeliveryRepository;
import com.dropit.deliveriesmanagment.delivery.repositories.TimeSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class DeliveryService {

    final int MAX_DELIVERIES_PER_TIMESLOT = 2;

    final int MAX_DELIVERIES_PER_DAY = 10;

    private final GeopifyService geopifyService;

    private final TimeSlotRepository timeSlotRepository;

    private final DeliveryRepository deliveryRepository;

    @Autowired
    public DeliveryService(GeopifyService geopifyService, TimeSlotRepository timeSlotRepository, DeliveryRepository deliveryRepository) {
        this.geopifyService = geopifyService;
        this.timeSlotRepository = timeSlotRepository;
        this.deliveryRepository = deliveryRepository;
    }

    public AddressDto resolveAddress(String searchTerm) {
        return geopifyService.resolveAddress(searchTerm);
    }

    public List<TimeSlot> getAvailableTimeSlots(AddressDto address) {
        return timeSlotRepository.findBySupportedCountryAndOccupied(address.getCountry(), false);
    }

    private Set<TimeSlot> activeTimeslotsBooking = new HashSet<>();

    public void bookDelivery(BookDeliveryDto bookDeliveryDto) {
        Optional<TimeSlot> timeslot = timeSlotRepository.findById(bookDeliveryDto.getTimeslotId());

        if (!timeslot.isPresent()) {
            throw new IllegalStateException("Timeslot id not exist");
        }

        if (getDeliveryCount(timeslot.get().getStartTime()) > MAX_DELIVERIES_PER_DAY) {
            throw new IllegalStateException("Only " + MAX_DELIVERIES_PER_DAY + " deliveries per day allowed");
        }

        signAsActiveBooking(timeslot.get());

        long deliveryCount = deliveryRepository.countDeliveryDtoByTimeSlot(timeslot.get());
        if (deliveryCount == MAX_DELIVERIES_PER_TIMESLOT) {
            throw new IllegalStateException("Timeslot is occupied");
        }
        deliveryRepository.save(new Delivery(timeslot.get(), Status.PENDING));
        signOfFromBooking(timeslot.get());
    }

    private synchronized void signAsActiveBooking(TimeSlot timeslot) {
        if (activeTimeslotsBooking.contains(timeslot)) {
            try {
                Thread.sleep(2000); //TODO : should handle it more properly
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } else {
            activeTimeslotsBooking.add(timeslot);
        }
    }

    private synchronized void signOfFromBooking(TimeSlot timeslot) {
        activeTimeslotsBooking.remove(timeslot);
    }


    public void updateDeliveryStatus(long deliveryId, Status status) {
        deliveryRepository.updateStatus(deliveryId, status);
    }

    public void deleteDelivery(long deliveryId) {
        deliveryRepository.deleteById(deliveryId);
    }

    public List<Delivery> getTodaysDeliveries() {
        LocalDate localDate = LocalDate.now();
        LocalDateTime startDay = localDate.atStartOfDay();
        LocalDateTime endOfDay = localDate.atTime(LocalTime.MAX);
        List<TimeSlot> timeslots = timeSlotRepository.findTimeSlotDtoByStartTimeIsBetween(startDay, endOfDay);

        return deliveryRepository.findDeliveriesByTimeSlotIn(timeslots);
    }

    public List<Delivery> getWeekDeliveries() {
        LocalDate today = LocalDate.now();
        LocalDate nextWeek = today.plus(1, ChronoUnit.WEEKS);

        List<TimeSlot> timeslots = timeSlotRepository.findTimeSlotDtoByStartTimeIsAfterAndStartTimeIsBefore(today.atStartOfDay(), nextWeek.atTime(LocalTime.MAX));
        return deliveryRepository.findDeliveriesByTimeSlotIn(timeslots);
    }

    private int getDeliveryCount(LocalDateTime startTime) {
        LocalDate date = LocalDate.from(startTime);
        List<TimeSlot> timeslots = timeSlotRepository.findTimeSlotDtoByStartTimeIsAfterAndStartTimeIsBefore(date.atStartOfDay(), date.atTime(LocalTime.MAX));
        return deliveryRepository.countDeliveriesByTimeSlotIn(timeslots);
    }
}
