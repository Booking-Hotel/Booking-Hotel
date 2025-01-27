package com.hotel.booking.domain.accommodation.dto;

import com.hotel.booking.domain.accommodation.entity.Accommodation;
import com.hotel.booking.domain.location.entity.Location;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class AccommodationListResponseDto {
    private Long accommodationId;
    private String name;
    private Integer star;
    private Double rating;
    private String amenities;
    private String address;
    private String imgUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public AccommodationListResponseDto(Accommodation accommodation, Location location) {
        this.accommodationId = accommodation.getId();
        this.name = accommodation.getName();
        this.star = accommodation.getStar();
        this.rating = accommodation.getRating();
        this.amenities = accommodation.getAmenities();
        this.address = location.getAddress();
        this.imgUrl = accommodation.getImgUrl();
        this.createdAt = accommodation.getCreatedAt();
        this.updatedAt = accommodation.getUpdatedAt();
    }
}
