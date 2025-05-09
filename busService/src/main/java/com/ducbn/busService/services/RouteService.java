package com.ducbn.busService.services;

import com.ducbn.busService.dtos.RouteDTO;
import com.ducbn.busService.models.Route;
import com.ducbn.busService.repositories.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RouteService implements IRouteService {
    private final RouteRepository routeRepository;

    @Override
    public Route createRoute(RouteDTO routeDTO) {
        Route route = Route.builder()
                .origin(routeDTO.getOrigin())
                .destination(routeDTO.getDestination())
                .distance(routeDTO.getDistance())
                .build();
        return routeRepository.save(route);
    }

    @Override
    public List<Route> getAllRoutes() {
        return routeRepository.findAll();
    }

    @Override
    public Route getRouteById(Long id) {
        return routeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Route not found with id: " + id));
    }

    @Override
    public Route updateRoute(Long id, RouteDTO routeDTO) {
        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Route not found with id: " + id));

        route.setOrigin(routeDTO.getOrigin());
        route.setDestination(routeDTO.getDestination());
        route.setDistance(routeDTO.getDistance());

        return routeRepository.save(route);
    }

    @Override
    public void deleteRoute(Long id) {
        if (!routeRepository.existsById(id)) {
            throw new RuntimeException("Route not found with id: " + id);
        }
        routeRepository.deleteById(id);
    }
}
