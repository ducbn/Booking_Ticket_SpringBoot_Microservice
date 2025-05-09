package com.ducbn.busService.repositorys;

import com.ducbn.busService.models.BusImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BusImageRepository extends JpaRepository<BusImage, Long> {
    List<BusImage> findByBusId(Long busId);
}
