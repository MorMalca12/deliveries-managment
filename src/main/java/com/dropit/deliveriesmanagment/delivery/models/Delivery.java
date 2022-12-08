package com.dropit.deliveriesmanagment.delivery.models;


import com.dropit.deliveriesmanagment.delivery.enums.Status;
import jakarta.persistence.*;


@Entity
public class Delivery {

    @Id
    @GeneratedValue
    private long id;
    @ManyToOne()
    @JoinColumn(name="TIME_SLOT_ID")
    private Timeslot timeslot;
    @Enumerated(EnumType.STRING)
    private Status status;

    public Delivery(Timeslot timeSlot, Status status) {
        this.timeslot = timeSlot;
        this.status = status;
    }

    public Delivery() {
    }

    public Timeslot getTimeSlot() {
        return timeslot;
    }

    public void setTimeSlot(Timeslot timeSlot) {
        this.timeslot = timeSlot;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
