package com.dropit.deliveriesmanagment.delivery.models;

import com.dropit.deliveriesmanagment.util.DateParser;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.LocalDateTime;


@Entity
public class TimeSlot {
    @Id
    @GeneratedValue
    private Long id;
    @JsonDeserialize(using = DateParser.class)
    private LocalDateTime startTime;
    @JsonDeserialize(using = DateParser.class)
    private LocalDateTime endTime;
    private String supportedCountry;

    private boolean occupied = false;

    public TimeSlot(LocalDateTime startTime, LocalDateTime endTime, String supportedCountry, boolean occupied) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.supportedCountry = supportedCountry;
        this.occupied = occupied;
    }


    public TimeSlot() {

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
