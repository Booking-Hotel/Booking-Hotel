package com.hotel.booking.domain.accommodation.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class AccommodationSearchResponseDto {
    private Long accommodationId;
    private String name;
    private Integer star;
    private Double rating;
    private String amenities;
    private Integer price;
    private String address;
    private String imgUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
