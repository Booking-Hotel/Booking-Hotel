package com.hotel.booking.domain.room.entity;

import com.hotel.booking.domain.room.entity.Room;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class RoomPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Column(nullable = false)
    private String imgUrl;
}

