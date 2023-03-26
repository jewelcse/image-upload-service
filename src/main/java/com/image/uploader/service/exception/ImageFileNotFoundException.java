package com.image.uploader.service.exception;

public class ImageFileNotFoundException extends RuntimeException {
    public ImageFileNotFoundException(String message) {
        super(message);
    }

}
