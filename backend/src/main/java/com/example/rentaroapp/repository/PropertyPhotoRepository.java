package com.example.rentaroapp.repository;

import com.example.rentaroapp.model.PropertyPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PropertyPhotoRepository extends JpaRepository<PropertyPhoto, Long> {

    List<PropertyPhoto> findByPropertyIdOrderByOrderIndexAsc(Long propertyId);

    Optional<PropertyPhoto> findByPropertyIdAndIsCoverTrue(Long propertyId);
}
