package com.ponir.portfolio.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@Service
public class ImageStorageService {
    private static final long MAX_IMAGE_SIZE = 10L * 1024 * 1024;
    private static final Map<String, String> ALLOWED_TYPES = Map.of(
            "image/jpeg", ".jpg",
            "image/png", ".png",
            "image/webp", ".webp",
            "image/gif", ".gif",
            "image/avif", ".avif"
    );

    private final Path uploadRoot;

    public ImageStorageService(@Value("${portfolio.storage.upload-directory:uploads}") String uploadDirectory) {
        this.uploadRoot = Path.of(uploadDirectory).toAbsolutePath().normalize();
    }

    @PostConstruct
    void initialize() {
        try {
            Files.createDirectories(uploadRoot);
        } catch (IOException exception) {
            throw new ImageStorageException("Could not initialize the image upload directory.", exception);
        }
    }

    public String store(MultipartFile image, String category) {
        if (image == null || image.isEmpty()) return null;
        if (image.getSize() > MAX_IMAGE_SIZE) {
            throw new ImageStorageException("Image files must be 10 MB or smaller.");
        }

        String contentType = image.getContentType();
        String extension = contentType == null ? null : ALLOWED_TYPES.get(contentType.toLowerCase(Locale.ROOT));
        if (extension == null) {
            throw new ImageStorageException("Choose a JPG, PNG, WebP, GIF, or AVIF image.");
        }
        if (category == null || !category.matches("[a-z0-9-]{2,30}")) {
            throw new ImageStorageException("The image category is invalid.");
        }

        Path categoryDirectory = uploadRoot.resolve(category).normalize();
        if (!categoryDirectory.startsWith(uploadRoot)) {
            throw new ImageStorageException("The image destination is invalid.");
        }

        String filename = UUID.randomUUID() + extension;
        Path destination = categoryDirectory.resolve(filename);
        try {
            Files.createDirectories(categoryDirectory);
            try (InputStream input = image.getInputStream()) {
                Files.copy(input, destination, StandardCopyOption.REPLACE_EXISTING);
            }
            return "/uploads/" + category + "/" + filename;
        } catch (IOException exception) {
            throw new ImageStorageException("The image could not be saved. Please try again.", exception);
        }
    }

    public String resourceLocation() {
        return uploadRoot.toUri().toString();
    }
}
