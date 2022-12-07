package com.dropit.deliveriesmanagment.delivery.models;


import com.dropit.deliveriesmanagment.delivery.enums.Status;
import jakarta.persistence.*;


@Entity
public class Delivery {

    @Id
    @GeneratedValue
    private long id;
    @OneToOne(cascade= CascadeType.ALL)//one-to-one
    @JoinColumn(name="TIME_SLOT_ID")
    private TimeSlot timeSlot;
    @Enumerated(EnumType.STRING)
    private Status status;

    public Delivery(TimeSlot timeSlot, Status status) {
        this.timeSlot = timeSlot;
        this.status = status;
    }

    public Delivery() {
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
