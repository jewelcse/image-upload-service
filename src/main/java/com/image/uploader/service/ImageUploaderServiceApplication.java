package com.image.uploader.service;

import com.image.uploader.service.service.ImageService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import java.io.File;

@SpringBootApplication
@EnableAsync
@AllArgsConstructor
public class ImageUploaderServiceApplication implements CommandLineRunner {

	private final ImageService imageService;

	public static void main(String[] args) {
		SpringApplication.run(ImageUploaderServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		File initializedFile = new File(".initialized");
		if (!initializedFile.exists()) {
			imageService.folderInitialize();;
			initializedFile.createNewFile();
		}

	}
}
