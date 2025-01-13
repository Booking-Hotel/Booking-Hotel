package com.hotel.booking.domain.reservation.entity;

import com.hotel.booking.domain.room.entity.Room;
import com.hotel.booking.domain.user.entity.User;
import com.hotel.booking.global.entity.BaseEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
public class Reservation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Column(nullable = false, name = "check_in_date")
    private LocalDate checkInDate;

    @Column(nullable = false, name = "check_out_date")
    private LocalDate checkOutDate;

    @Column(nullable = false)
    private String status;

}

