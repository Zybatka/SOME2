package edu.platform.service;

import edu.platform.dto.response.MediaAssetResponse;
import edu.platform.entity.Course;
import edu.platform.entity.MediaAsset;
import edu.platform.entity.User;
import edu.platform.exception.ResourceNotFoundException;
import edu.platform.mapper.MediaAssetMapper;
import edu.platform.media.config.MediaStorageProperties;
import edu.platform.media.storage.FileStorage;
import edu.platform.repository.CourseRepository;
import edu.platform.repository.MediaAssetRepository;
import edu.platform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class MediaService {

    private final MediaAssetRepository mediaAssetRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final MediaAssetMapper mediaAssetMapper;
    private final FileStorage fileStorage;
    private final MediaStorageProperties mediaProps;

    @Transactional
    public MediaAssetResponse upload(Long ownerId, Long courseId, MultipartFile file) {
        validateFile(file);
        try {
            byte[] bytes = file.getBytes();
            String checksum = sha256Hex(bytes);

            // Avoid duplicates by checksum
            MediaAsset existing = mediaAssetRepository.findByChecksum(checksum).orElse(null);
            if (existing != null) {
                return mediaAssetMapper.toResponse(existing);
            }

            User owner = userRepository.findById(ownerId)
                    .orElseThrow(() -> new ResourceNotFoundException("Owner not found: " + ownerId));
            Course course = null;
            if (courseId != null) {
                course = courseRepository.findById(courseId)
                        .orElseThrow(() -> new ResourceNotFoundException("Course not found: " + courseId));
            }

            String path = fileStorage.save(file.getOriginalFilename(), file.getContentType(), file.getSize(), new ByteArrayInputStream(bytes));

            MediaAsset asset = MediaAsset.builder()
                    .owner(owner)
                    .course(course)
                    .fileName(file.getOriginalFilename())
                    .filePath(path)
                    .fileSize(file.getSize())
                    .contentType(file.getContentType())
                    .checksum(checksum)
                    .build();

            MediaAsset saved = mediaAssetRepository.save(asset);
            return mediaAssetMapper.toResponse(saved);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file", e);
        }
    }

    @Transactional(readOnly = true)
    public MediaAssetResponse getMetadata(Long id) {
        MediaAsset asset = mediaAssetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Media asset not found: " + id));
        return mediaAssetMapper.toResponse(asset);
    }

    @Transactional(readOnly = true)
    public InputStreamResource download(Long id) {
        MediaAsset asset = mediaAssetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Media asset not found: " + id));
        InputStream in = fileStorage.load(asset.getFilePath());
        return new InputStreamResource(in);
    }

    @Transactional
    public void delete(Long id) {
        MediaAsset asset = mediaAssetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Media asset not found: " + id));
        fileStorage.delete(asset.getFilePath());
        mediaAssetRepository.delete(asset);
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("File size exceeds 5MB");
        }
        String ct = file.getContentType();
        java.util.Set<String> allowed = getAllowedContentTypes();
        if (ct == null || !allowed.contains(ct)) {
            throw new IllegalArgumentException("Unsupported content type: " + ct);
        }
    }

    private java.util.Set<String> getAllowedContentTypes() {
        java.util.Set<String> defaults = new java.util.HashSet<>(java.util.Arrays.asList("image/png", "image/jpeg", "image/webp"));
        List<String> list = mediaProps.getAllowedContentTypes();
        if (list == null || list.isEmpty()) return defaults;
        return new java.util.HashSet<>(list);
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
