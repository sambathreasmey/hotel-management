package com.hotel.system.hotel_management.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "room_type_images")
@Getter
@Setter
public class RoomTypeImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long roomTypeId;

    private String imageUrl;

    private Boolean isCover;

    private Integer displayOrder;
}