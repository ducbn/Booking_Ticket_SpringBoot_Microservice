package com.ducbn.busService.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusDTO {
    @JsonProperty("name")
    @NotBlank(message = "Bus name is required")
    private String name;

    @JsonProperty("route_id")
    @NotNull(message = "Route ID is required")
    private Long routeId;

    @JsonProperty("company_id")
    @NotNull(message = "Company ID is required")
    private Long companyId;

    @JsonProperty("bus_type_id")
    @NotNull(message = "Bus Type ID is required")
    private Long busTypeId;

    @JsonProperty("departure_time")
    @NotNull(message = "Departure time is required")
    private LocalTime departureTime;

    @JsonProperty("arrival_time")
    @NotNull(message = "Arrival time is required")
    private LocalTime arrivalTime;

    @JsonProperty("price")
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;

}
