package com.hotel.booking.global.secutiry.jwt.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class JwtDTO {
    private String userRole;
    private String userName;
}
