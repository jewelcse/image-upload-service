package com.image.uploader.service.serviceImpl;

import com.image.uploader.service.entity.Image;
import com.image.uploader.service.mapper.ImageMapper;
import com.image.uploader.service.model.responseDto.ImageUploadResponse;
import com.image.uploader.service.repository.ImageRepository;
import com.image.uploader.service.service.ImageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@AllArgsConstructor
@Service
public class ImageServiceImpl implements ImageService {

    private final Path uploadFolder = Paths.get("uploads");

    private final ImageRepository imageRepository;
    private final ImageMapper imageMapper;


    @Override
    public void folderInitialize() {
        try {
            Files.createDirectory(uploadFolder);
        } catch (IOException e) {
            throw new RuntimeException("Could not created the folder!");
        }
    }

    @Override
    public ImageUploadResponse save(MultipartFile file) {
        Image newImage = new Image();
        Image savedImage;
        try {
            long timeInMillis = new Date().getTime();
            String filename = Objects.requireNonNull(file.getOriginalFilename()).toLowerCase().replaceAll(" ", "_");
            String finalFileName = timeInMillis + "_" + filename;
            Files.copy(file.getInputStream(), this.uploadFolder.resolve(finalFileName));
            // set the image values
            newImage.setOriginalFileName(finalFileName);
            // save image to db
            savedImage = imageRepository.save(newImage);

        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
        if (savedImage.getId() != null) {
            //todo: publish event for every image
        }
        return imageMapper.toImageUploadResponse(savedImage);
    }

    @Override
    public List<ImageUploadResponse> uploads(MultipartFile[] files) {
        List<ImageUploadResponse> imageUploadResponses = new ArrayList<>();
        for (MultipartFile file : files) {
            imageUploadResponses.add(save(file));
        }
        return imageUploadResponses;
    }


}
