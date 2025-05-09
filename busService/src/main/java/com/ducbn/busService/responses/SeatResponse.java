package com.ducbn.busService.responses;

import com.ducbn.busService.models.Seat;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SeatResponse {
    private String seatNumber;
    private Boolean isAvailable;

    public static SeatResponse fromSeat(Seat seat) {
        return SeatResponse.builder()
                .seatNumber(seat.getSeatNumber())
                .isAvailable(seat.getIsAvailable())
                .build();
    }
}
