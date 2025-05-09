package com.ducbn.busService.services;

import com.ducbn.busService.dtos.BusDTO;
import com.ducbn.busService.models.Bus;
import com.ducbn.busService.responses.BusResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


public interface IBusService {
    Bus createBus(BusDTO busDTO);

    List<String> createBusImages(Long busId, List<MultipartFile> files) throws Exception;

    Page<BusResponse> getAllBuses(PageRequest pageRequest);

    boolean existsByName(String busName);

    Bus getBusById(Long id);

    Bus updateBus(Long id, BusDTO busDTO);

    void deleteBus(Long id);
}
