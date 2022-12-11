package com.dropit.deliveriesmanagment.delivery.repositories;

import com.dropit.deliveriesmanagment.delivery.models.Delivery;
import com.dropit.deliveriesmanagment.delivery.enums.Status;
import com.dropit.deliveriesmanagment.delivery.models.Timeslot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface DeliveryRepository extends JpaRepository<Delivery,Long> {
    List<Delivery> findDeliveriesByTimeslotIn(List<Timeslot> timeslots);

    int countDeliveriesByTimeslotIn(List<Timeslot> timeslots);
    @Modifying
    @Transactional
    @Query("update Delivery d set d.status = :status where d.id = :id")
    void updateStatus(@Param(value = "id") long id, @Param(value = "status") Status status);

}
