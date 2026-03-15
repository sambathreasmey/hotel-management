package com.hotel.system.hotel_management.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "rate_plans")
@Getter
@Setter
public class RatePlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long roomTypeId;

    @ManyToOne
    @JoinColumn(name = "rate_plan_master_id")
    private RatePlanMaster ratePlanMaster;

    private BigDecimal price;
}