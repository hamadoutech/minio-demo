package com.hamadoutech.demominio.service;

import com.hamadoutech.demominio.config.MinioPropertiesConfig;
import com.hamadoutech.demominio.exception.FileNotFoundException;
import com.hamadoutech.demominio.exception.InvalidFileException;
import com.hamadoutech.demominio.exception.MinioException;
import com.hamadoutech.demominio.model.Fichier;
import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.ListObjectsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.Result;
import io.minio.messages.Item;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class MinioService implements MinioServiceI {

    public static final List<String>
            TYPE_FICHIERS_AUTHORISE = Arrays.asList("image/jpeg", "image/png", "application/pdf", "text/plain");
    public static final int MAX_SIZE = 10 * 1024 * 1024;

    private final MinioClient minioClient;
    private final MinioPropertiesConfig propertiesConfig;

    public MinioService(final MinioClient minioClient, final MinioPropertiesConfig propertiesConfig) {
        this.minioClient = minioClient;
        this.propertiesConfig = propertiesConfig;
    }

    @Override
    public void ajouterFichier(final String fileName, final InputStream inputStream, final String contentType) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(propertiesConfig.bucketName())
                            .object(fileName)
                            .stream(inputStream, inputStream.available(), -1)
                            .contentType(contentType)
                            .build()
            );
        } catch (Exception e) {
            throw new InvalidFileException(String.format("Erreur %s lors de l'upload du fichier %s : ", e.getMessage(), fileName));
        }

    }

    @Override
    public void supprimerFichier(final String nom) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(propertiesConfig.bucketName())
                            .object(nom)
                            .build()
            );
        } catch (Exception e) {
            throw new FileNotFoundException("Erreur lors de la suppression : " + nom);
        }
    }

    @Override
    public InputStream telechargerFichier(final String nom) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(propertiesConfig.bucketName())
                            .object(nom)
                            .build()
            );
        } catch (Exception e) {
            throw new FileNotFoundException("Fichier non trouvé : " + nom);
        }
    }

    @Override
    public List<Fichier> listerFichiers() {
        List<Fichier> files = new ArrayList<>();
        try {
            Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(propertiesConfig.bucketName())
                            .build());
            for (Result<Item> result : results) {
                files.add(new Fichier(result.get().objectName()));
            }
        } catch (Exception e) {
            throw new FileNotFoundException("Erreur lors du listage");
        }
        return files;
    }

   /* @PostConstruct
    public void init() {
        try {
            boolean exists = minioClient.bucketExists(
                    BucketExistsArgs.builder().bucket(propertiesConfig.bucketName()).build()
            );
            if (!exists) {
                minioClient.makeBucket(
                        MakeBucketArgs.builder().bucket(propertiesConfig.bucketName()).build()
                );
            }
        } catch (Exception e) {
            throw new MinioException("Impossible de créer le bucket");
        }
    }*/

    public void validerFichierAvantAjout(final MultipartFile file) {
        if (file.isEmpty()) {
            throw new InvalidFileException("Le fichier est vide");
        }
        if (file.getSize() > MAX_SIZE) {
            throw new InvalidFileException("Fichier trop volumineux");
        }
        String contentType = file.getContentType();
        if (!TYPE_FICHIERS_AUTHORISE.contains(contentType)) {
            throw new InvalidFileException(String.format("Type de fichier %s non autorisé", contentType));
        }
    }
}
