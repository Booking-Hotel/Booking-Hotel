package com.hotel.booking.domain.user.entity;

import com.hotel.booking.domain.role.entity.Role;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.processing.Pattern;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String birth;
}
