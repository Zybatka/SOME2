package edu.platform.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "Media asset metadata")
public class MediaAssetResponse {
    private Long id;
    private UserResponse owner;
    private CourseResponse course;
    private String fileName;
    private String filePath;
    private Long fileSize;
    private String contentType;
    private String checksum;
    private LocalDateTime createdAt;
}

