package com.hotel.system.hotel_management.service;

import com.hotel.system.hotel_management.dto.BedConfigDTO;
import com.hotel.system.hotel_management.dto.RatePlanDTO;
import com.hotel.system.hotel_management.dto.RoomSetupRequest;
import com.hotel.system.hotel_management.entity.*;
import com.hotel.system.hotel_management.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RoomSetupService {

    private final RoomTypeRepository roomTypeRepository;
    private final RoomRepository roomRepository;
    private final RatePlanRepository ratePlanRepository;

    private final RoomTypeMasterRepository roomTypeMasterRepository;
    private final RatePlanMasterRepository ratePlanMasterRepository;

    private final RoomTypeAmenityRepository roomTypeAmenityRepository;
    private final RoomTypeImageRepository roomTypeImageRepository;
    private final BedConfigRepository bedConfigRepository;

    private final BedTypeMasterRepository bedTypeMasterRepository;

    private final FileStorageService fileStorageService;
    private final FileRepository fileRepository;

    @Transactional
    public void setupRoom(RoomSetupRequest request) {

        // 1️⃣ Validate room type
        RoomTypeMaster master = roomTypeMasterRepository
                .findById(request.getRoomTypeMasterId())
                .orElseThrow(() -> new RuntimeException("Room type not found"));

        // 2️⃣ Create room type
        RoomType roomType = new RoomType();
        roomType.setHotelId(request.getHotelId());
        roomType.setRoomTypeMaster(master);

        roomTypeRepository.save(roomType);

        // 3️⃣ Save related data
        saveAmenities(roomType, request);
        saveBeds(roomType, request);
        attachImages(roomType, request);
        saveRooms(roomType, request);
        saveRatePlans(roomType, request);
    }

    private void saveAmenities(RoomType roomType, RoomSetupRequest request) {

        if (request.getAmenityIds() == null || request.getAmenityIds().isEmpty()) {
            return;
        }

        List<RoomTypeAmenity> amenities = new ArrayList<>();

        for (Long amenityId : request.getAmenityIds()) {

            RoomTypeAmenity amenity = new RoomTypeAmenity();
            amenity.setRoomTypeId(roomType.getId());
            amenity.setAmenity_master_id(amenityId);

            amenities.add(amenity);
        }

        roomTypeAmenityRepository.saveAll(amenities);
    }

    private void saveBeds(RoomType roomType, RoomSetupRequest request) {

        if (request.getBeds() == null || request.getBeds().isEmpty()) {
            return;
        }

        List<RoomTypeBedConfig> beds = new ArrayList<>();

        for (BedConfigDTO dto : request.getBeds()) {

            BedTypeMaster bedType = bedTypeMasterRepository
                    .findById(dto.getBedTypeId())
                    .orElseThrow(() -> new RuntimeException("Bed type not found"));

            RoomTypeBedConfig config = new RoomTypeBedConfig();
            config.setRoomTypeId(roomType.getId());
            config.setBedTypeMasterId(bedType.getId());
            config.setBedCount(dto.getBedCount());

            beds.add(config);
        }

        bedConfigRepository.saveAll(beds);
    }

    private void saveRooms(RoomType roomType, RoomSetupRequest request) {

        if (request.getRoomNumbers() == null || request.getRoomNumbers().isEmpty()) {
            return;
        }

        List<Room> rooms = new ArrayList<>();

        for (String number : request.getRoomNumbers()) {

            Room room = new Room();
            room.setRoomTypeId(roomType.getId());
            room.setRoomNumber(number);
            room.setStatus("available");

            rooms.add(room);
        }

        roomRepository.saveAll(rooms);
    }

    private void saveRatePlans(RoomType roomType, RoomSetupRequest request) {

        if (request.getRatePlans() == null || request.getRatePlans().isEmpty()) {
            return;
        }

        List<RatePlan> ratePlans = new ArrayList<>();

        for (RatePlanDTO dto : request.getRatePlans()) {

            RatePlanMaster master = ratePlanMasterRepository
                    .findById(dto.getRatePlanMasterId())
                    .orElseThrow(() -> new RuntimeException("Rate plan not found"));

            RatePlan ratePlan = new RatePlan();
            ratePlan.setRoomTypeId(roomType.getId());
            ratePlan.setRatePlanMaster(master);
            ratePlan.setPrice(dto.getPrice());

            ratePlans.add(ratePlan);
        }

        ratePlanRepository.saveAll(ratePlans);
    }

    private void attachImages(RoomType roomType, RoomSetupRequest request) {

        List<RoomTypeImage> images = new ArrayList<>();

        // Cover image
        if (request.getCoverImageId() != null) {

            FileEntity file = fileRepository
                    .findById(request.getCoverImageId())
                    .orElseThrow();

            RoomTypeImage cover = new RoomTypeImage();
            cover.setRoomTypeId(roomType.getId());
            cover.setImageUrl(file.getFilePath());
            cover.setIsCover(true);
            cover.setDisplayOrder(1);

            images.add(cover);

            file.setStatus("ACTIVE");
        }

        // Gallery images
        if (request.getImageIds() != null) {

            int order = 2;

            for (Long fileId : request.getImageIds()) {

                FileEntity file = fileRepository
                        .findById(fileId)
                        .orElseThrow();

                RoomTypeImage img = new RoomTypeImage();
                img.setRoomTypeId(roomType.getId());
                img.setImageUrl(file.getFilePath());
                img.setIsCover(false);
                img.setDisplayOrder(order++);

                images.add(img);

                file.setStatus("ACTIVE");
            }
        }

        roomTypeImageRepository.saveAll(images);
    }
}