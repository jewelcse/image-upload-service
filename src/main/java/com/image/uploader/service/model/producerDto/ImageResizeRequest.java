package com.image.uploader.service.model.producerDto;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class ImageResizeRequest{
    private Long id;
    private String originalFileName;

}
