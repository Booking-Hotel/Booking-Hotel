package com.hotel.booking.domain.user.repository;

import com.hotel.booking.domain.role.entity.Role;
import com.hotel.booking.domain.user.entity.User;
import java.util.Optional;
import java.util.Random;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByRole(Role role);
}
