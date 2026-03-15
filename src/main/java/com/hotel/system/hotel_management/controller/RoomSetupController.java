package com.hotel.system.hotel_management.controller;

import com.hotel.system.hotel_management.dto.RoomSetupRequest;
import com.hotel.system.hotel_management.service.RoomSetupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/merchant/rooms")
@RequiredArgsConstructor
public class RoomSetupController {

    private final RoomSetupService roomSetupService;

    @PostMapping("/setup")
    public ResponseEntity<String> setupRoom(
            @RequestBody RoomSetupRequest request
    ) {

        roomSetupService.setupRoom(request);

        return ResponseEntity.ok("Room setup completed");
    }
}
