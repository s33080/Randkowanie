package com.example.datingapp.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImageService {
    private final String UPLOAD_DIR = "uploads/";

    public String saveImage(MultipartFile file, Long userId) throws IOException {
        if (file.isEmpty()) return null;

        // Tworzy folder, jeśli nie istnieje
        File directory = new File(UPLOAD_DIR);
        if (!directory.exists()) directory.mkdirs();

        // Nazwa pliku: user_1_nazwa.jpg
        String fileName = "user_" + userId + "_" + file.getOriginalFilename();
        Path path = Paths.get(UPLOAD_DIR + fileName);

        Files.write(path, file.getBytes()); // Zapis fizyczny na dysku
        return fileName; // Zwracamy nazwę do zapisu w bazie
    }
}