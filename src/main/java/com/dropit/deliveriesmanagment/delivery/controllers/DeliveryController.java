package com.dropit.deliveriesmanagment.delivery.controllers;

import com.dropit.deliveriesmanagment.delivery.dto.*;
import com.dropit.deliveriesmanagment.delivery.enums.Status;
import com.dropit.deliveriesmanagment.delivery.models.Delivery;
import com.dropit.deliveriesmanagment.delivery.models.TimeSlot;
import com.dropit.deliveriesmanagment.delivery.services.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DeliveryController {

    private DeliveryService deliveryService;

    @Autowired
    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }


    @PostMapping("/resolve-address")
    public AddressDto resolveAddress(@RequestBody String searchTerm) {
        return deliveryService.resolveAddress(searchTerm);
    }

    @PostMapping("/timeslots")
    public List<TimeSlot> retrieveAvailableTimeSlots(@RequestBody AddressDto address) {
        return deliveryService.getAvailableTimeSlots(address);
    }

    @PostMapping("/deliveries")
    public void bookDelivery(@RequestBody BookDeliveryDto bookDeliveryDto) {
        deliveryService.bookDelivery(bookDeliveryDto);
    }

    @PostMapping("/deliveries/{DELIVERY_ID}/complete")
    public void completeDelivery(@PathVariable(value = "DELIVERY_ID") long deliveryId) {
        deliveryService.updateDeliveryStatus(deliveryId, Status.COMPLETED);
    }

    @DeleteMapping("/deliveries/{DELIVERY_ID}/cancel")
    public void cancelDelivery(@PathVariable(value = "DELIVERY_ID") long deliveryId) {
        deliveryService.deleteDelivery(deliveryId);
    }

    @GetMapping("/deliveries/daily")
    public List<Delivery> getTodayDeliveries() {
        return deliveryService.getTodaysDeliveries();
    }

    @GetMapping("/deliveries/weekly")
    public List<Delivery> getWeekDeliveries() {
        return deliveryService.getWeekDeliveries();
    }


}
