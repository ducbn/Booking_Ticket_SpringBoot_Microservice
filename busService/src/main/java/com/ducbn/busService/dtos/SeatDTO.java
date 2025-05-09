package com.ducbn.busService.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeatDTO {
    @JsonProperty("bus_id")
    @NotNull(message = "Bus ID is required")
    private Long busId;

    @JsonProperty("seat_number")
    @NotBlank(message = "Seat number is required")
    private String seatNumber;

    @JsonProperty("is_available")
    private Boolean isAvailable = true;
}
