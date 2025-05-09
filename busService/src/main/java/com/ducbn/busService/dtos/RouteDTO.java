package com.ducbn.busService.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RouteDTO {

    @JsonProperty("origin")
    @NotBlank(message = "Origin must not be blank")
    @Size(max = 100, message = "Origin must not exceed 100 characters")
    private String origin;

    @JsonProperty("destination")
    @NotBlank(message = "Destination must not be blank")
    @Size(max = 100, message = "Destination must not exceed 100 characters")
    private String destination;

    @JsonProperty("distance")
    @NotNull(message = "Distance must not be null")
    @Min(value = 1, message = "Distance must be greater than 0")
    private Float distance;
}
