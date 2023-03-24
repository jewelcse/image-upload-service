package com.image.uploader.service.service;

import com.image.uploader.service.model.responseDto.ImageUploadResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {

    ImageUploadResponse upload(MultipartFile file) throws IOException;
}
