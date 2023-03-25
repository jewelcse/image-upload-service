package com.image.uploader.service.controller;


import com.image.uploader.service.service.ImageService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/images")
@AllArgsConstructor
public class ImageUploadController {

    private final ImageService imageService;

    @PostMapping()
    public ResponseEntity<?> uploadImages(@RequestParam("files") MultipartFile[] files) {

        for (MultipartFile file : files) {
            String contentType = file.getContentType();
            if (!contentType.startsWith("image/")) {
                throw new IllegalArgumentException("Invalid file type. Only image files are allowed.");
            }
        }
        try {
            imageService.uploads(files);
            return ResponseEntity.ok().body("File uploaded successfully!");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file!");
        }

    }


}
