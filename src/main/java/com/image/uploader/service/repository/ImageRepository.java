package com.image.uploader.service.repository;

import com.image.uploader.service.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image,Long> {
    Boolean existsByOriginalFileName(String fileName);
    Boolean existsByThumbnailFileName(String fileName);
}
