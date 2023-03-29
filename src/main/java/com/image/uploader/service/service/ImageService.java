package com.image.uploader.service.service;

import com.image.uploader.service.model.producerDto.ImageResizeRequest;
import com.image.uploader.service.model.responseDto.ImageUploadResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public interface ImageService {

    void folderInitialize();
    ImageUploadResponse save(MultipartFile file);
    List<ImageUploadResponse> uploads(MultipartFile[] file) throws IOException;

    Resource loadImage(String fileName);

    void resizeImage(ImageResizeRequest request);
}
