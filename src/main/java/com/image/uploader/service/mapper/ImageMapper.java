package com.image.uploader.service.mapper;


import com.image.uploader.service.entity.Image;
import com.image.uploader.service.model.responseDto.ImageUploadResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ImageMapper {


    public ImageUploadResponse toImageUploadResponse(Image image) {
        if (image == null) return null;
        return ImageUploadResponse.builder()
                .fileName(image.getFileName())
                .originalFilePath(image.getOriginalFilePath())
                .resizedFilePath(image.getResizedFilePath())
                .build();
    }

}
