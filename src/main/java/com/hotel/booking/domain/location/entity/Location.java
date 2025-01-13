package com.hotel.booking.domain.location.entity;

import com.hotel.booking.domain.accommodation.entity.Accommodation;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "accommodation_id", nullable = false)
    private Accommodation accommodation;

    @Column(nullable = false)
    private String address;

    private Double latitude;

    private Double longitude;
}
