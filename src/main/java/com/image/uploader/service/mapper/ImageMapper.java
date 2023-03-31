package com.image.uploader.service.mapper;


import com.image.uploader.service.entity.Image;
import com.image.uploader.service.model.responseDto.ImageUploadResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ImageMapper {


    public ImageUploadResponse toImageUploadResponse(Image image) {
        if (image == null) return null;
        return ImageUploadResponse.builder()
                .thumbnailFileName(image.getThumbnailFileName())
                .imageTitle(image.getImageTitle())
                .originalFileName(image.getOriginalFileName())
                .build();
    }

    public List<ImageUploadResponse> toListOfImagesUploadResponse(List<Image> images) {

        if (images == null) return null;
        List<ImageUploadResponse> responses = new ArrayList<>();
        for (Image image : images) {
            responses.add(toImageUploadResponse(image));
        }
        return responses;
    }

}
