package com.hotel.booking.domain.accommodation.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hotel.booking.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalTime;

@Entity
@Data
public class Accommodation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "accommodation_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    private Integer star;

    @Column(nullable = false)
    private Double rating;

    @Column(nullable = false)
    private String summary;

    @Column(nullable = false)
    private Integer roomCount;

    @Column(nullable = false)
    private String amenities;

    @JsonFormat(pattern = "HH:mm")
    @Column(nullable = false)
    private LocalTime checkIn;

    @JsonFormat(pattern = "HH:mm")
    @Column(nullable = false)
    private LocalTime checkOut;

    @Column(nullable = false)
    private String reviews;

    @Column(nullable = false)
    private String imgUrl;

}

