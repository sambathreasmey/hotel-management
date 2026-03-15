package com.hotel.system.hotel_management.repository;

import com.hotel.system.hotel_management.entity.RatePlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatePlanRepository extends JpaRepository<RatePlan, Long> {
}