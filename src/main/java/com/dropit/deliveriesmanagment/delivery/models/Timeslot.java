package com.dropit.deliveriesmanagment.delivery.models;

import com.dropit.deliveriesmanagment.util.DateParser;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
public class Timeslot {
    @Id
    @GeneratedValue
    private Long id;
    @JsonDeserialize(using = DateParser.class)
    private LocalDateTime startTime;
    @JsonDeserialize(using = DateParser.class)
    private LocalDateTime endTime;
    private String supportedCountry;

    @OneToMany(mappedBy = "timeslot", cascade = CascadeType.ALL)
    private List<Delivery> deliveries = new ArrayList<>();


    private boolean occupied = false;

    public Timeslot(LocalDateTime startTime, LocalDateTime endTime, String supportedCountry, List<Delivery> deliveries, boolean occupied) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.supportedCountry = supportedCountry;
        this.deliveries = deliveries;
        this.occupied = occupied;
    }


    public Timeslot() {

    }

    public List<Delivery> getDeliveries() {
        return deliveries;
    }

    public void setDeliveries(List<Delivery> deliveries) {
        this.deliveries = deliveries;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getSupportedCountry() {
        return supportedCountry;
    }

    public void setSupportedCountry(String supportedCountry) {
        this.supportedCountry = supportedCountry;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }


}
