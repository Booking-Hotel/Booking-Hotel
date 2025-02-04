package com.hotel.booking.domain.accommodation.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class AccommodationSearchReqeustDto {
    private String keyword;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Integer people;
    private Integer roomCount;

    public AccommodationSearchReqeustDto(String keyword, LocalDate checkInDate, LocalDate checkOutDate, Integer people, Integer roomCount) {
        this.keyword = keyword;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.people = people;
        this.roomCount = roomCount;
    }
}
