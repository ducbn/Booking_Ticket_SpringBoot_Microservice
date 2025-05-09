package com.ducbn.busService.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BusTypeDTO {

    @JsonProperty("name")
    @NotBlank(message = "Bus type name must not be blank")
    @Size(max = 100, message = "Bus type name must not exceed 100 characters")
    private String name;

    @JsonProperty("description")
    @Size(max = 255, message = "Description must not exceed 255 characters")
    private String description;

    @JsonProperty("seat_capacity")
    @NotNull(message = "Seat capacity must not be null")
    @Min(value = 1, message = "Seat capacity must be at least 1")
    private Long seatCapacity;
}

