package com.ducbn.busService.controllers;

import com.ducbn.busService.responses.SeatResponse;
import com.ducbn.busService.services.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seats")
@RequiredArgsConstructor
public class SeatController {
    private final SeatService seatService;

    @GetMapping("/bus/{busId}")
    public ResponseEntity<List<SeatResponse>> getAllSeatsByBus(@PathVariable Long busId) {
        return ResponseEntity.ok(seatService.getAllSeatsByBusId(busId));
    }

    @GetMapping("/bus/{busId}/available")
    public ResponseEntity<List<SeatResponse>> getAvailableSeatsByBus(@PathVariable Long busId) {
        return ResponseEntity.ok(seatService.getAvailableSeatsByBusId(busId));
    }
}
