package edu.platform.media.storage;

import java.io.InputStream;

public interface FileStorage {
    String save(String originalFileName, String contentType, long size, InputStream contentStream);
    InputStream load(String storagePath);
    void delete(String storagePath);
}

