package com.ducbn.busService.services;

import com.ducbn.busService.dtos.BusTypeDTO;
import com.ducbn.busService.models.BusType;

import java.util.List;

public interface IBusTypeService {
    BusType createBusType(BusTypeDTO busTypeDTO);
    List<BusType> getAllBusTypes();
    BusType getBusTypeById(Long id);
    BusType updateBusType(Long id, BusTypeDTO busTypeDTO);
    void deleteBusType(Long id);
}
