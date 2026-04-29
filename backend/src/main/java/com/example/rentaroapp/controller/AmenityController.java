package com.example.rentaroapp.controller;

import com.example.rentaroapp.model.Amenity;
import com.example.rentaroapp.service.AmenityService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/amenities")
@RequiredArgsConstructor
public class AmenityController {

    private final AmenityService amenityService;

    @GetMapping
    public ResponseEntity<List<Amenity>> getAll() {
        return ResponseEntity.ok(amenityService.getAll());
    }

    @PostMapping
    public ResponseEntity<Amenity> create(@RequestBody CreateAmenityRequest request) {
        Amenity amenity = amenityService.create(request.name(), request.icon());
        return ResponseEntity.status(HttpStatus.CREATED).body(amenity);
    }

    public record CreateAmenityRequest(
            @NotBlank String name,
            String icon
    ) {}
}
