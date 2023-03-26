package com.image.uploader.service.producer;


import com.image.uploader.service.model.producerDto.ImageResizeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import static com.image.uploader.service.util.ApplicationConstants.IMAGE_RESIZE_TOPIC;

@RequiredArgsConstructor
@Service
public class ImageResizeEventProduce {

    private final KafkaTemplate<String, ImageResizeRequest> producer;

    public void resizeEventPublish(ImageResizeRequest request){
        Message<ImageResizeRequest> message = MessageBuilder
                .withPayload(request)
                .setHeader(KafkaHeaders.TOPIC,IMAGE_RESIZE_TOPIC)
                .build();
        producer.send(message);
    }

}
