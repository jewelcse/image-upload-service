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
import java.util.UUID;

@AllArgsConstructor
@Service
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final ImageMapper imageMapper;

    @Override
    public ImageUploadResponse upload(MultipartFile file) throws IOException {


        // Generate unique filename for uploaded image
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        String filename = UUID.randomUUID().toString() + "." + extension;

        byte[] bytes = file.getBytes();
        Path path = Paths.get("uploads/" + originalFilename);
        Files.write(path, bytes);


        // Save file location to database
        Image imageFile = new Image();
        imageFile.setFileName(originalFilename);
        imageFile.setOriginalFilePath(path.toAbsolutePath().toString());
        imageFile.setResizedFilePath("");

        Image uploadedImage = imageRepository.save(imageFile);
        return imageMapper.toImageUploadResponse(uploadedImage);
    }
}
