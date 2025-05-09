package com.ducbn.busService.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CompanyDTO {

    @JsonProperty("name")
    @NotBlank(message = "Company name must not be blank")
    @Size(max = 100, message = "Company name must not exceed 100 characters")
    private String name;

    @JsonProperty("contact_info")
    @Size(max = 255, message = "Contact info must not exceed 255 characters")
    private String contactInfo;
}
