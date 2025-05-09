package com.ducbn.busService.services;

import com.ducbn.busService.dtos.SeatDTO;
import com.ducbn.busService.models.Bus;
import com.ducbn.busService.models.Seat;
import com.ducbn.busService.repositorys.BusRepository;
import com.ducbn.busService.repositorys.SeatRepository;
import com.ducbn.busService.responses.SeatResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatService implements ISeatService {

    private final SeatRepository seatRepository;
    private final BusRepository busRepository;

    @Override
    public List<SeatResponse> getAllSeatsByBusId(Long busId) {
        Bus bus = busRepository.findById(busId)
                .orElseThrow(() -> new RuntimeException("Bus not found"));

        return bus.getSeats().stream()
                .map(SeatResponse::fromSeat)
                .toList();
    }

    @Override
    public List<SeatResponse> getAvailableSeatsByBusId(Long busId) {
        Bus bus = busRepository.findById(busId)
                .orElseThrow(() -> new RuntimeException("Bus not found"));

        return bus.getSeats().stream()
                .filter(Seat::getIsAvailable)
                .map(SeatResponse::fromSeat)
                .toList();
    }
}
