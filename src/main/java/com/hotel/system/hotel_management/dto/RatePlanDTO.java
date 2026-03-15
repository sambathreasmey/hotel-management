package com.hotel.system.hotel_management.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RatePlanDTO {

    private Long ratePlanMasterId;

    private BigDecimal price;
}