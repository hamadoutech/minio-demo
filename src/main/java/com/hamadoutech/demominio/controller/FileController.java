package com.hamadoutech.demominio.controller;

import com.hamadoutech.demominio.model.Fichier;
import com.hamadoutech.demominio.service.MinioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Gestion de Fichiers", description = "API pour gérer les fichiers dans MinIO")
public class FileController {

    private final MinioService minioService;

    public FileController(final MinioService minioService) {
        this.minioService = minioService;
    }

    @Operation(summary = "Liste des fichiers", description = "Récupère la liste de tous les fichiers dans le bucket MinIO")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des fichiers récupérée avec succès",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Fichier.class))),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @GetMapping("/list")
    public ResponseEntity<List<Fichier>> listerFichier() {
        try {
            List<Fichier> fichiers = minioService.listerFichiers();
            return ResponseEntity.ok(fichiers);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ArrayList<>());
        }
    }

    @Operation(summary = "Uploader un fichier", description = "Charge un fichier dans le bucket MinIO")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fichier uploadé avec succès"),
            @ApiResponse(responseCode = "400", description = "Fichier invalide"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @PostMapping("/upload")
    public ResponseEntity<String> ajouterFichier(@Parameter(description = "Fichier à uploader", required = true) @RequestParam("file") MultipartFile file) {
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

    @Operation(summary = "Télécharger un fichier", description = "Télécharge un fichier spécifique depuis le bucket MinIO")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fichier téléchargé avec succès",
                    content = @Content(mediaType = "application/octet-stream")),
            @ApiResponse(responseCode = "404", description = "Fichier non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> telechargerFichier(@Parameter(description = "Nom du fichier à télécharger", required = true) @PathVariable String fileName) {
        InputStream fileStream = minioService.telechargerFichier(fileName);

        InputStreamResource resource = new InputStreamResource(fileStream);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @Operation(summary = "Supprimer un fichier", description = "Supprime un fichier spécifique du bucket MinIO")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fichier supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Fichier non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @DeleteMapping("/delete/{fileName}")
    public ResponseEntity<String> supprimerFichier(@Parameter(description = "Nom du fichier à supprimer", required = true) @PathVariable String fileName) {
        try {
            minioService.supprimerFichier(fileName);
            return ResponseEntity.ok("Fichier supprimé avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur : " + e.getMessage());
        }
    }
}
