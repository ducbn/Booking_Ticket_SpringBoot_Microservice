package com.ducbn.busService.controllers;

import com.ducbn.busService.dtos.BusTypeDTO;
import com.ducbn.busService.models.BusType;
import com.ducbn.busService.services.BusTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bus-types")
@RequiredArgsConstructor
public class BusTypeController {
    private final BusTypeService busTypeService;

    @PostMapping("")
    public ResponseEntity<?> createBusType(
            @Valid @RequestBody BusTypeDTO busTypeDTO,
            BindingResult result)
    {
        try{
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            BusType newBusType = busTypeService.createBusType(busTypeDTO);
            return ResponseEntity.ok(newBusType);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("")
    public ResponseEntity<List<BusType>> getAllBusTypes() {
        return ResponseEntity.ok(busTypeService.getAllBusTypes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBusTypeById(@PathVariable Long id) {
        try{
            BusType busType = busTypeService.getBusTypeById(id);
            return ResponseEntity.ok(busType);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBusType(@PathVariable Long id, @Valid @RequestBody BusTypeDTO busTypeDTO, BindingResult result) {
        try{
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            BusType updatedBusType = busTypeService.updateBusType(id, busTypeDTO);
            return ResponseEntity.ok(updatedBusType);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBusType(@PathVariable Long id) {
        try{
            busTypeService.deleteBusType(id);
            return ResponseEntity.ok(String.format("BusType id = %s deleted", id));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
