package com.hamadoutech.demominio.exception;

public class InvalidFileException extends MinioException {
    public InvalidFileException(String message) {
        super(message);
    }
}
