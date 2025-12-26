package com.hamadoutech.demominio.exception;

public class FileNotFoundException extends MinioException {
    public FileNotFoundException(String filename) {
        super("Fichier non trouvé : " + filename);
    }
}
