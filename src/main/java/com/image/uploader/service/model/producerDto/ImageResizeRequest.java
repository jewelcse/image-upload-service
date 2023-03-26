package com.image.uploader.service.model.producerDto;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class ImageResizeRequest implements Serializable {
    private static final long serialVersionUID = 4658763458638335614L;

    private Long id;
    private byte[] imageData;
}
