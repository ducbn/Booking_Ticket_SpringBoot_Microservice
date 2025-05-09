package com.ducbn.busService.responses;

import lombok.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BusListResponse {
    private List<BusResponse> buses;
    private int totalPages;
}
