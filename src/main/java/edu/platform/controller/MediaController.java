package edu.platform.controller;

import edu.platform.dto.response.MediaAssetResponse;
import edu.platform.security.UserPrincipal;
import edu.platform.service.MediaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/media")
@RequiredArgsConstructor
@Tag(name = "Media", description = "Media storage endpoints")
public class MediaController {

    private final MediaService mediaService;

    @Operation(summary = "Upload image (PNG/JPG/WEBP)")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MediaAssetResponse> upload(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "courseId", required = false) Long courseId) {
        MediaAssetResponse response = mediaService.upload(currentUser.getId(), courseId, file);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get media metadata")
    @GetMapping("/{id}")
    public ResponseEntity<MediaAssetResponse> getMetadata(@PathVariable Long id) {
        return ResponseEntity.ok(mediaService.getMetadata(id));
    }

    @Operation(summary = "Download media file")
    @GetMapping("/{id}/download")
    public ResponseEntity<InputStreamResource> download(@PathVariable Long id) {
        MediaAssetResponse meta = mediaService.getMetadata(id);
        InputStreamResource resource = mediaService.download(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(meta.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + meta.getFileName() + "\"")
                .body(resource);
    }
}

