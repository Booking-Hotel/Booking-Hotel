package com.hotel.booking.domain.roomfacility.entity;

import com.hotel.booking.domain.room.entity.Room;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class RoomFacility {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    private Boolean water;
    private Boolean laundryService;
    private Boolean netflix;
    private Boolean wifi;
    private Boolean disposableProduct;
    private Integer bathCount;
    private Integer bedCount;
}
