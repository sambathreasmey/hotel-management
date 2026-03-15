package com.hotel.system.hotel_management.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "room_type_bed_configs")
@Getter
@Setter
public class RoomTypeBedConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long roomTypeId;

    private Long bedTypeMasterId;

    private Integer bedCount;
}
