package com.image.uploader.service.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(value = InvalidImageFileException.class)
    public ResponseEntity<ExceptionResponse> invalidImageFileException(InvalidImageFileException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponse(ex.getMessage()));
    }

    @ExceptionHandler(value = ImageFileNotFoundException.class)
    public ResponseEntity<ExceptionResponse> imageFileNotFoundException(ImageFileNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ExceptionResponse(ex.getMessage()));
    }
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ExceptionResponse {
        private String message;
    }


}
