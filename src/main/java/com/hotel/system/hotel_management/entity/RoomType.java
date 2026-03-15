package com.hotel.system.hotel_management.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "room_types")
@Getter
@Setter
public class RoomType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long hotelId;

    @ManyToOne
    @JoinColumn(name = "room_type_master_id")
    private RoomTypeMaster roomTypeMaster;
}