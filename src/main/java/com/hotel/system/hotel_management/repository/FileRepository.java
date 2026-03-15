package com.hotel.system.hotel_management.repository;

import com.hotel.system.hotel_management.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {

    @Query("""
        SELECT f FROM FileEntity f
        WHERE f.status='TEMP'
        AND f.createdAt < :time
    """)
    List<FileEntity> findExpiredTempFiles(LocalDateTime time);
}
