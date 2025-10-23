package edu.platform.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "Lesson response")
public class LessonResponse {
    private Long id;
    private CourseResponse course;
    private String title;
    private String content;
    private Integer orderIndex;
    private Long version;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

