package com.ducbn.busService.repositorys;

import com.ducbn.busService.models.Bus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BusRepository extends JpaRepository<Bus, Long> {
    // Tìm bus theo tên (chính xác)
    List<Bus> findByName(String name);

    boolean existsByName(String name);
}
