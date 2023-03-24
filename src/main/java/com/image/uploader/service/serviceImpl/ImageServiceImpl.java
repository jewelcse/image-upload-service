package com.image.uploader.service.serviceImpl;

import com.image.uploader.service.model.responseDto.ImageUploadResponse;
import com.image.uploader.service.repository.ImageRepository;
import com.image.uploader.service.service.ImageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@AllArgsConstructor
@Service
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;

    @Override
    public ImageUploadResponse upload(MultipartFile file){


        return null;
    }
}
