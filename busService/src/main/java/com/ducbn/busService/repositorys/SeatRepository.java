package com.ducbn.busService.repositorys;

import com.ducbn.busService.models.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    @Query("SELECT COUNT(s) FROM Seat s WHERE s.bus.id = :busId AND s.isAvailable = true")
    int countAvailableSeatsByBusId(@Param("busId") Long busId);

    List<Seat> findByBusId(Long busId);
}

