package com.image.uploader.service.consumer;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.image.uploader.service.model.producerDto.ImageResizeRequest;
import com.image.uploader.service.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import static com.image.uploader.service.util.ApplicationConstants.IMAGE_RESIZE_GROUP;
import static com.image.uploader.service.util.ApplicationConstants.IMAGE_RESIZE_TOPIC;


@RequiredArgsConstructor
@Service
public class ImageResizeEventConsume {

    private final ImageService imageService;
    private final SimpMessagingTemplate messagingTemplate;

    @KafkaListener(
            topics = IMAGE_RESIZE_TOPIC,
            groupId = IMAGE_RESIZE_GROUP
    )
    public void resizeEventConsume(ImageResizeRequest request) {
        try {
            imageService.resizeImage(request);
            Thread.sleep(1000);
            String message = "Image Resize Successfully completed!";
            String jsonMessage = new ObjectMapper().writeValueAsString(message);
            messagingTemplate.convertAndSend("/topic/messages", jsonMessage);
        } catch (Exception e) {

        }

    }

}
