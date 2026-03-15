package com.hotel.system.hotel_management.repository;

import com.hotel.system.hotel_management.entity.RoomTypeBedConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BedConfigRepository extends JpaRepository<RoomTypeBedConfig, Long> {
}
