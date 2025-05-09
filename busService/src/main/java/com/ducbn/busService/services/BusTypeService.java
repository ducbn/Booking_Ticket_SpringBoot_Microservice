package com.ducbn.busService.services;

import com.ducbn.busService.dtos.BusTypeDTO;
import com.ducbn.busService.models.BusType;
import com.ducbn.busService.repositories.BusTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BusTypeService implements IBusTypeService {
    private final BusTypeRepository busTypeRepository;

    @Override
    public BusType createBusType(BusTypeDTO busTypeDTO) {
        BusType busType = BusType.builder()
                .name(busTypeDTO.getName())
                .description(busTypeDTO.getDescription())
                .seatCapacity(busTypeDTO.getSeatCapacity())
                .build();
        return busTypeRepository.save(busType);
    }

    @Override
    public List<BusType> getAllBusTypes() {
        return busTypeRepository.findAll();
    }

    @Override
    public BusType getBusTypeById(Long id) {
        return busTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BusType not found with id: " + id));
    }

    @Override
    public BusType updateBusType(Long id, BusTypeDTO busTypeDTO) {
        BusType busType = busTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BusType not found with id: " + id));

        busType.setName(busTypeDTO.getName());
        busType.setDescription(busTypeDTO.getDescription());
        busType.setSeatCapacity(busTypeDTO.getSeatCapacity());

        return busTypeRepository.save(busType);
    }

    @Override
    public void deleteBusType(Long id) {
        if (!busTypeRepository.existsById(id)) {
            throw new RuntimeException("BusType not found with id: " + id);
        }
        busTypeRepository.deleteById(id);
    }
}
