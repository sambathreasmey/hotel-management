package com.hotel.system.hotel_management.controller;

import com.hotel.system.hotel_management.dto.FileUploadResponse;
import com.hotel.system.hotel_management.entity.FileEntity;
import com.hotel.system.hotel_management.repository.FileRepository;
import com.hotel.system.hotel_management.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageUploadController {

    private final FileStorageService fileStorageService;
    private final FileRepository fileRepository;

    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public FileUploadResponse upload(
            @RequestParam("file") MultipartFile file
    ) {

        String path = fileStorageService.saveFile(file);

        FileEntity entity = new FileEntity();
        entity.setFileName(file.getOriginalFilename());
        entity.setFilePath(path);
        entity.setStatus("TEMP");

        fileRepository.save(entity);

        return new FileUploadResponse(entity.getId());
    }
}
