package com.hamadoutech.demominio.service;

import com.hamadoutech.demominio.model.Fichier;

import java.io.InputStream;
import java.util.List;

public interface MinioServiceI {

    void ajouterFichier(String nom, InputStream inputStream, String contentType);

    void supprimerFichier(String nom);

    InputStream telechargerFichier(String nom);

    List<Fichier> listerFichiers();
}
