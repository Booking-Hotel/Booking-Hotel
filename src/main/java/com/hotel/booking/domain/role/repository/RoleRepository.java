package com.hotel.booking.domain.role.repository;

import com.hotel.booking.domain.role.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByUserName(String userName);
}
