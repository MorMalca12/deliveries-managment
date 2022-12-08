package com.dropit.deliveriesmanagment.delivery.repositories;

import com.dropit.deliveriesmanagment.delivery.models.Timeslot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TimeSlotRepository extends JpaRepository<Timeslot,Long> {

    List<Timeslot> findBySupportedCountryAndOccupied(String supportedCountry, boolean isOccupied);

    List<Timeslot> findTimeslotByStartTimeIsBetween(LocalDateTime startTime, LocalDateTime endTime);






}
