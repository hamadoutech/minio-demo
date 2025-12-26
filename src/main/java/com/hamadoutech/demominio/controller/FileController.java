package com.hamadoutech.demominio.controller;

import com.hamadoutech.demominio.model.Fichier;
import com.hamadoutech.demominio.service.MinioService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("v1/minio")
public class FileController {

    private final MinioService minioService;

    public FileController(final MinioService minioService) {
        this.minioService = minioService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<Fichier>> listerFichier() {
        try {
            List<Fichier> fichiers = minioService.listerFichiers();
            return ResponseEntity.ok(fichiers);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ArrayList<>());
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<String> ajouterFichier(@RequestParam("file") MultipartFile file) {
        // Validation
        minioService.validerFichierAvantAjout(file);
        try {
            minioService.ajouterFichier(
                    file.getOriginalFilename(),
                    file.getInputStream(),
                    file.getContentType()
            );
            return ResponseEntity.ok("Fichier uploadé avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur : " + e.getMessage());
        }
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> telechargerFichier(@PathVariable String fileName) {
        InputStream fileStream = minioService.telechargerFichier(fileName);

        InputStreamResource resource = new InputStreamResource(fileStream);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @DeleteMapping("/delete/{fileName}")
    public ResponseEntity<String> supprimerFichier(@PathVariable String fileName) {
        try {
            minioService.supprimerFichier(fileName);
            return ResponseEntity.ok("Fichier supprimé avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur : " + e.getMessage());
        }
    }
}
