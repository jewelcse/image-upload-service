package com.image.uploader.service.service;

import com.image.uploader.service.model.responseDto.ImageUploadResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageService {

    void folderInitialize();

    ImageUploadResponse save(MultipartFile file);

    List<ImageUploadResponse> uploads(MultipartFile[] file) throws IOException;


}
