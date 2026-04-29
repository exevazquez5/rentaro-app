package com.example.rentaroapp.controller;

import com.example.rentaroapp.model.Property;
import com.example.rentaroapp.model.PropertyType;
import com.example.rentaroapp.service.PropertyService;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/properties")
@RequiredArgsConstructor
public class PropertyController {

    private final PropertyService propertyService;

    @PostMapping
    public ResponseEntity<Property> create(@RequestBody CreatePropertyRequest request) {
        Property property = propertyService.create(
                request.hostId(),
                request.title(),
                request.description(),
                request.address(),
                request.city(),
                request.country(),
                request.pricePerNight(),
                request.maxGuests(),
                request.bedrooms(),
                request.bathrooms(),
                request.propertyType()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(property);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Property> getById(@PathVariable Long id) {
        return ResponseEntity.ok(propertyService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<Property>> search(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut
    ) {
        List<Property> results;
        if (city != null && checkIn != null && checkOut != null) {
            results = propertyService.searchByCityAndDates(city, checkIn, checkOut);
        } else if (city != null) {
            results = propertyService.searchByCity(city);
        } else {
            results = propertyService.searchByCity("");
        }
        return ResponseEntity.ok(results);
    }

    @GetMapping("/host/{hostId}")
    public ResponseEntity<List<Property>> getByHost(@PathVariable Long hostId) {
        return ResponseEntity.ok(propertyService.listByHost(hostId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Property> update(@PathVariable Long id, @RequestBody UpdatePropertyRequest request) {
        Property property = propertyService.update(
                id,
                request.title(),
                request.description(),
                request.pricePerNight(),
                request.maxGuests(),
                request.bedrooms(),
                request.bathrooms(),
                request.propertyType()
        );
        return ResponseEntity.ok(property);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        propertyService.delete(id);
        return ResponseEntity.noContent().build();
    }

    public record CreatePropertyRequest(
            @NotNull Long hostId,
            @NotBlank String title,
            String description,
            @NotBlank String address,
            @NotBlank String city,
            @NotBlank String country,
            @NotNull @DecimalMin("0.01") BigDecimal pricePerNight,
            @NotNull @Min(1) Integer maxGuests,
            @NotNull @Min(0) Integer bedrooms,
            @NotNull @Min(0) Integer bathrooms,
            @NotNull PropertyType propertyType
    ) {}

    public record UpdatePropertyRequest(
            String title,
            String description,
            @DecimalMin("0.01") BigDecimal pricePerNight,
            @Min(1) Integer maxGuests,
            @Min(0) Integer bedrooms,
            @Min(0) Integer bathrooms,
            PropertyType propertyType
    ) {}
}
