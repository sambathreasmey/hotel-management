package com.hotel.system.hotel_management.dto;

import lombok.Data;

@Data
public class ImageDTO {

    private String imageUrl;

    private Boolean isCover;

    private Integer displayOrder;
}
