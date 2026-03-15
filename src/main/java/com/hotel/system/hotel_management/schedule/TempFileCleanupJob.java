package com.hotel.system.hotel_management.schedule;

import com.hotel.system.hotel_management.entity.FileEntity;
import com.hotel.system.hotel_management.repository.FileRepository;
import com.hotel.system.hotel_management.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TempFileCleanupJob {

    private final FileRepository fileRepository;
    private final FileStorageService fileStorageService;

    @Scheduled(cron = "0 1/5 * * * *")
//    @Scheduled(cron = "0 0 * * * *")
    public void cleanup() {

        System.out.println("Cleanup job running...");

        LocalDateTime time = LocalDateTime.now().minusMinutes(2);
//        LocalDateTime time = LocalDateTime.now().minusHours(2);

        List<FileEntity> files = fileRepository.findExpiredTempFiles(time);

        for(FileEntity file : files){

            fileStorageService.delete(file.getFilePath());

            fileRepository.delete(file);
        }
    }
}