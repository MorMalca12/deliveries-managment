package com.dropit.deliveriesmanagment.delivery.dto;

public class BookDeliveryDto {

    private UserDto user;
    private long timeslotId;

    public BookDeliveryDto(UserDto user, int timeslotId) {
        this.user = user;
        this.timeslotId = timeslotId;
    }

    public BookDeliveryDto() {
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public long getTimeslotId() {
        return timeslotId;
    }

    public void setTimeslotId(int timeslotId) {
        this.timeslotId = timeslotId;
    }
}
