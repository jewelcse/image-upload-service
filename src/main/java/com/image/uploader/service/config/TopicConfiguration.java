package com.image.uploader.service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import static com.image.uploader.service.util.ApplicationConstraints.IMAGE_RESIZE_TOPIC;

@Configuration
public class TopicConfiguration {


    @Bean
    public NewTopic imageResizeTopic() {
        return TopicBuilder
                .name(IMAGE_RESIZE_TOPIC)
                .build();
    }

}
