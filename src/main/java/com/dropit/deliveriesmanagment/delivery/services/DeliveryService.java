package com.dropit.deliveriesmanagment.delivery.services;


import com.dropit.deliveriesmanagment.delivery.dto.AddressDto;
import com.dropit.deliveriesmanagment.delivery.dto.BookDeliveryDto;
import com.dropit.deliveriesmanagment.delivery.enums.Status;
import com.dropit.deliveriesmanagment.delivery.models.Delivery;
import com.dropit.deliveriesmanagment.delivery.models.Timeslot;
import com.dropit.deliveriesmanagment.delivery.repositories.DeliveryRepository;
import com.dropit.deliveriesmanagment.delivery.repositories.TimeSlotRepository;
import com.dropit.deliveriesmanagment.util.LockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.locks.Lock;

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

    public List<Timeslot> getAvailableTimeSlots(AddressDto address) {
        return timeSlotRepository.findBySupportedCountryAndOccupied(address.getCountry(), false);
    }


    public void bookDelivery(BookDeliveryDto bookDeliveryDto) {
        long timeslotId = bookDeliveryDto.getTimeslotId();

        Lock timeslotLock = LockService.getLock(String.valueOf(timeslotId));
        timeslotLock.lock();

        Timeslot timeslot = timeSlotRepository.findById(timeslotId).get();

        try {
            if (timeslot == null) {
                throw new IllegalStateException("Timeslot id not exist");
            }

            if (getDeliveryCount(timeslot.getStartTime()) > MAX_DELIVERIES_PER_DAY) {
                throw new IllegalStateException("Only " + MAX_DELIVERIES_PER_DAY + " deliveries per day allowed");
            }


            if (timeslot.isOccupied()) {
                throw new IllegalStateException("Timeslot is occupied");
            }

            List<Delivery> deliveries = timeslot.getDeliveries();
            deliveries.add(new Delivery(timeslot, Status.PENDING));
            if (deliveries.size() == MAX_DELIVERIES_PER_TIMESLOT)
                timeslot.setOccupied(true);

            timeSlotRepository.save(timeslot);
        }finally {
            timeslotLock.unlock();
            LockService.removeLock(String.valueOf(timeslotId));
        }

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
        List<Timeslot> timeslots = timeSlotRepository.findTimeslotByStartTimeIsBetween(startDay, endOfDay);

        return deliveryRepository.findDeliveriesByTimeslotIn(timeslots);
    }

    public List<Delivery> getWeekDeliveries() {
        LocalDate today = LocalDate.now();
        LocalDate nextWeek = today.plus(1, ChronoUnit.WEEKS);

        List<Timeslot> timeslots = timeSlotRepository.findTimeslotByStartTimeIsBetween(today.atStartOfDay(), nextWeek.atTime(LocalTime.MAX));
        return deliveryRepository.findDeliveriesByTimeslotIn(timeslots);
    }

    private int getDeliveryCount(LocalDateTime startTime) {
        LocalDate date = LocalDate.from(startTime);
        List<Timeslot> timeslots = timeSlotRepository.findTimeslotByStartTimeIsBetween(date.atStartOfDay(), date.atTime(LocalTime.MAX));

        return deliveryRepository.countDeliveriesByTimeslotIn(timeslots);
    }

}
