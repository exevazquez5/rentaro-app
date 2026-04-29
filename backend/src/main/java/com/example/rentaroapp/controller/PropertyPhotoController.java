package com.example.rentaroapp.controller;

import com.example.rentaroapp.model.PropertyPhoto;
import com.example.rentaroapp.service.PropertyPhotoService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/properties/{propertyId}/photos")
@RequiredArgsConstructor
public class PropertyPhotoController {

    private final PropertyPhotoService propertyPhotoService;

    @PostMapping
    public ResponseEntity<PropertyPhoto> addPhoto(@PathVariable Long propertyId, @RequestBody AddPhotoRequest request) {
        PropertyPhoto photo = propertyPhotoService.addPhoto(propertyId, request.url(), request.orderIndex());
        return ResponseEntity.status(HttpStatus.CREATED).body(photo);
    }

    @PutMapping("/{photoId}/cover")
    public ResponseEntity<PropertyPhoto> setCoverPhoto(@PathVariable Long photoId) {
        return ResponseEntity.ok(propertyPhotoService.setCoverPhoto(photoId));
    }

    @DeleteMapping("/{photoId}")
    public ResponseEntity<Void> deletePhoto(@PathVariable Long photoId) {
        propertyPhotoService.deletePhoto(photoId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<PropertyPhoto>> getByProperty(@PathVariable Long propertyId) {
        return ResponseEntity.ok(propertyPhotoService.getByPropertyId(propertyId));
    }

    public record AddPhotoRequest(
            @NotBlank String url,
            Integer orderIndex
    ) {}
}
