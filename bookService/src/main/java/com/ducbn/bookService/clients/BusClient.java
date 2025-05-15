package com.ducbn.bookService.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "bus-service", url = "${bus.service.url}") // Đặt URL thật ở application.yml
public interface BusClient {

    @GetMapping("/buses/{busId}/available-seats")
    List<String> getAvailableSeats(@PathVariable("busId") Long busId);
}
