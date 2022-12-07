package com.dropit.deliveriesmanagment.delivery.repositories;

import com.dropit.deliveriesmanagment.delivery.models.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot,Long> {

    List<TimeSlot> findBySupportedCountryAndOccupied(String supportedCountry, boolean isOccupied);
    List<TimeSlot> findTimeSlotDtoByStartTimeIsAfterAndStartTimeIsBefore(LocalDateTime startTime, LocalDateTime endTime);

    List<TimeSlot> findTimeSlotDtoByStartTimeIsBetween(LocalDateTime startTime, LocalDateTime endTime);


}
