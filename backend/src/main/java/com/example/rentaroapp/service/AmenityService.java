package com.example.rentaroapp.service;

import com.example.rentaroapp.model.Amenity;
import com.example.rentaroapp.repository.AmenityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AmenityService {

    private final AmenityRepository amenityRepository;

    public List<Amenity> getAll() {
        return amenityRepository.findAll();
    }

    public Amenity create(String name, String icon) {
        return amenityRepository.findByName(name)
                .orElseGet(() -> {
                    Amenity amenity = new Amenity();
                    amenity.setName(name);
                    amenity.setIcon(icon);
                    return amenityRepository.save(amenity);
                });
    }
}
