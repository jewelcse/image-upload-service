package com.image.uploader.service.serviceImpl;

import com.image.uploader.service.entity.Image;
import com.image.uploader.service.exception.ImageFileNotFoundException;
import com.image.uploader.service.mapper.ImageMapper;
import com.image.uploader.service.model.producerDto.ImageResizeRequest;
import com.image.uploader.service.model.responseDto.ImageUploadResponse;
import com.image.uploader.service.producer.ImageResizeEventProduce;
import com.image.uploader.service.repository.ImageRepository;
import com.image.uploader.service.service.ImageService;
import lombok.AllArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@AllArgsConstructor
@Service
public class ImageServiceImpl implements ImageService {

    private final Path uploadFolder = Paths.get("uploads");
    private final Path thumbnailFolder = Paths.get("thumbnails");
    private final ImageRepository imageRepository;
    private final ImageMapper imageMapper;
    private final ImageResizeEventProduce messageProducer;

    @Override
    public void folderInitialize() {
        try {
            Files.createDirectory(uploadFolder);
            Files.createDirectory(thumbnailFolder);
        } catch (IOException e) {
            throw new RuntimeException("Could not created the folder!");
        }
    }

    @Async
    @Override
    public ImageUploadResponse save(MultipartFile file) {
        Image newImage = new Image();
        Image savedImage;
        String filename;
        String finalFileName;
        long timeInMillis = new Date().getTime();
        filename = Objects.requireNonNull(file.getOriginalFilename()).toLowerCase().replaceAll(" ", "_");
        finalFileName = timeInMillis + "_" + filename;
        //
        //Files.copy(file.getInputStream(), this.uploadFolder.resolve(finalFileName));
        //
        try (InputStream inputStream = file.getInputStream(); BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream); OutputStream outputStream = new FileOutputStream(this.uploadFolder.resolve(finalFileName).toFile()); BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream)) {

            byte[] buffer = new byte[4096]; // Use a buffer of 4KB
            int bytesRead;
            while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
                bufferedOutputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
        newImage.setOriginalFileName(finalFileName);
        savedImage = imageRepository.save(newImage);

        if (savedImage.getId() != null) {
            ImageResizeRequest request = ImageResizeRequest.builder().id(savedImage.getId()).originalFileName(finalFileName).build();
            messageProducer.resizeEventPublish(request);
        }
        return imageMapper.toImageUploadResponse(savedImage);
    }


    @Override
    public List<ImageUploadResponse> uploads(MultipartFile[] files) {
        List<ImageUploadResponse> imageUploadResponses = new ArrayList<>();
        for (MultipartFile file : files) {
            imageUploadResponses.add(save(file));
        }
        return imageUploadResponses;
    }

    @Override
    public Resource loadImage(String fileName) {
        String imageType = fileName.substring(0, 9);
        Boolean doesExist = imageType.equals("thumbnail") ? imageRepository.existsByThumbnailFileName(fileName) : imageRepository.existsByOriginalFileName(fileName);
        if (!doesExist) {
            throw new ImageFileNotFoundException("Image Not found!");
        }
        try {
            Path file = imageType.equals("thumbnail") ? thumbnailFolder.resolve(fileName) : uploadFolder.resolve(fileName);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Async
    @Override
    public void resizeImage(ImageResizeRequest request) {
        try {
            Resource loadedImage = loadImage(request.getOriginalFileName());
            String thumbnailFileName = "thumbnail_" + request.getOriginalFileName();
            Path thumbnailPath = thumbnailFolder.resolve(thumbnailFileName).normalize();
            if (Files.exists(thumbnailPath)) {
                return;
            }
            BufferedImage image = ImageIO.read(loadedImage.getInputStream());
            BufferedImage thumbnail = Thumbnails.of(image).size(200, 200).asBufferedImage();
            String[] writerFormats = ImageIO.getWriterFormatNames();
            String format = "";
            for (String writerFormat : writerFormats) {
                if (loadedImage.getFilename().endsWith(writerFormat)) {
                    format = writerFormat;
                    break;
                }
            }
            if (format.isEmpty()) {
                throw new RuntimeException("Could not determine image format!");
            }
            if (ImageIO.write(thumbnail, format, thumbnailPath.toFile())) {
                Optional<Image> saveImage = imageRepository.findById(request.getId());
                if (saveImage.isPresent()) {
                    saveImage.get().setThumbnailFileName(thumbnailFileName);
                    imageRepository.save(saveImage.get());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not resize the image. Error: " + e.getMessage());
        }
    }
}
