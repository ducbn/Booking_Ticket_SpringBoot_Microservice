package com.ducbn.busService.responses;

import com.ducbn.busService.models.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BusResponse {
    private Long id;
    private String name;
    private Long routeId;
    private String thumbnail;
    private Long companyId;
    private Long busTypeId;
    private LocalTime departureTime;
    private LocalTime arrivalTime;
    private BigDecimal price;
    private int availableSeats;

    public static BusResponse fromBus(Bus bus) {
        int availableSeatCount = 0;
        if (bus.getSeats() != null) {
            availableSeatCount = (int) bus.getSeats().stream()
                    .filter(Seat::getIsAvailable)
                    .count();
        }

        BusResponse busResponse = BusResponse.builder()
                .id(bus.getId())
                .name(bus.getName())
                .routeId(bus.getRoute().getId())
                .companyId(bus.getCompany().getId())
                .busTypeId(bus.getBusType().getId())
                .thumbnail(bus.getThumbnail())
                .departureTime(bus.getDepartureTime())
                .arrivalTime(bus.getArrivalTime())
                .price(bus.getPrice())
                .availableSeats(availableSeatCount)
                .build();
        return busResponse;
    }

}

