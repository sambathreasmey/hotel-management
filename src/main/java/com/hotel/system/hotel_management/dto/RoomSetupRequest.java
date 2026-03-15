package com.hotel.system.hotel_management.dto;

import lombok.Data;

import java.util.List;

@Data
public class RoomSetupRequest {

    private Long hotelId;

    private Long roomTypeMasterId;

    private List<String> roomNumbers;

    private List<Long> amenityIds;

    private List<BedConfigDTO> beds;

    private List<RatePlanDTO> ratePlans;

    private Long coverImageId;

    private List<Long> imageIds;
}
