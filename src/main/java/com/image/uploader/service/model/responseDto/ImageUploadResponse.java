package com.image.uploader.service.model.responseDto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class ImageUploadResponse {
    private String originalFileName;
    private String thumbnailFileName;
}
