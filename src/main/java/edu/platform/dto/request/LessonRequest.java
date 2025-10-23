package edu.platform.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Create/update lesson request")
public class LessonRequest {

    @NotNull
    @Schema(description = "Course ID", example = "1")
    private Long courseId;

    @NotBlank
    @Schema(description = "Lesson title", example = "Introduction")
    private String title;

    @Schema(description = "Lesson content (Markdown/HTML)")
    private String content;

    @NotNull
    @Schema(description = "Order index", example = "1")
    private Integer orderIndex;
}

