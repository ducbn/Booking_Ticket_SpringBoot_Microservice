package com.ducbn.busService.services;

import com.ducbn.busService.dtos.RouteDTO;
import com.ducbn.busService.models.Route;

import java.util.List;

public interface IRouteService {
    Route createRoute(RouteDTO routeDTO);
    List<Route> getAllRoutes();
    Route getRouteById(Long id);
    Route updateRoute(Long id, RouteDTO routeDTO);
    void deleteRoute(Long id);
}
