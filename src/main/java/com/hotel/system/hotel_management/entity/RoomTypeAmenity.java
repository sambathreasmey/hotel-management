package com.hotel.system.hotel_management.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "room_type_amenities")
@Getter
@Setter
public class RoomTypeAmenity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long roomTypeId;

    private Long amenity_master_id;
}
