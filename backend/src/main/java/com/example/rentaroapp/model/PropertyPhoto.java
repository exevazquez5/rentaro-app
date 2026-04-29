package com.example.rentaroapp.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "property_photos")
public class PropertyPhoto extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @Column(nullable = false)
    private String url;

    @Column(name = "is_cover", nullable = false)
    private Boolean isCover = false;

    @Column(name = "order_index", nullable = false)
    private Integer orderIndex = 0;
}
