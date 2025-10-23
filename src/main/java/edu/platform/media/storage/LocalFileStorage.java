package edu.platform.media.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

@Component
@RequiredArgsConstructor
@Slf4j
public class LocalFileStorage implements FileStorage {

    @Value("${media.storage.base-dir:storage/media}")
    private String baseDir;

    @Override
    public String save(String originalFileName, String contentType, long size, InputStream contentStream) {
        try {
            byte[] data = contentStream.readAllBytes();
            String checksum = sha256Hex(data);
            String ext = getExtension(originalFileName);
            String deterministicName = checksum + (ext.isEmpty() ? "" : ("." + ext));
            Path dir = Paths.get(baseDir, checksum.substring(0, 2), checksum.substring(2, 4));
            Files.createDirectories(dir);
            Path path = dir.resolve(deterministicName);
            if (!Files.exists(path)) {
                Files.write(path, data);
            }
            return path.toString();
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to store file", e);
        }
    }

    @Override
    public InputStream load(String storagePath) {
        try {
            return Files.newInputStream(Paths.get(storagePath));
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to load file", e);
        }
    }

    @Override
    public void delete(String storagePath) {
        try {
            Files.deleteIfExists(Paths.get(storagePath));
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to delete file", e);
        }
    }

    private static String getExtension(String name) {
        int i = name.lastIndexOf('.');
        if (i == -1) return "";
        return name.substring(i + 1);
    }

    private static String sha256Hex(byte[] data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(digest.digest(data));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}

