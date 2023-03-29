package com.image.uploader.service.controller;


import com.image.uploader.service.exception.InvalidImageFileException;
import com.image.uploader.service.model.responseDto.ImageUploadResponse;
import com.image.uploader.service.service.ImageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/images")
@AllArgsConstructor
public class ImageUploadController {

    private final ImageService imageService;


    @PostMapping
    public ResponseEntity<?> uploadImages(@RequestParam("files") MultipartFile[] files) {

        for (MultipartFile file : files) {
            String contentType = file.getContentType();
            if (!contentType.startsWith("image/")) {
                throw new InvalidImageFileException("Invalid file type. Only image files are allowed.");
            }
        }
        try {
           List<ImageUploadResponse> responses = imageService.uploads(files);
            return ResponseEntity.ok().body(responses);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file!");
        }

    }

    @GetMapping("/{fileName}")
    public ResponseEntity<?> loadImage(@PathVariable String fileName, HttpServletRequest request) {
        Resource resource = imageService.loadImage(fileName);
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            System.out.println("Could not determine file type.");
        }
        if(contentType == null) {
            contentType = "application/octet-stream";
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }


    //todo: notify api

}
