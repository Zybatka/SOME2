package edu.platform.controller;

import edu.platform.dto.request.LessonRequest;
import edu.platform.dto.response.LessonResponse;
import edu.platform.security.UserPrincipal;
import edu.platform.service.LessonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lessons")
@RequiredArgsConstructor
@Tag(name = "Lessons", description = "Lesson management endpoints")
public class LessonController {

    private final LessonService lessonService;

    @Operation(summary = "Create a lesson (TEACHER)")
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    @PostMapping
    public ResponseEntity<LessonResponse> create(@Valid @RequestBody LessonRequest request,
                                                 @AuthenticationPrincipal UserPrincipal currentUser) {
        return ResponseEntity.ok(lessonService.create(request));
    }

    @Operation(summary = "Get lesson by id")
    @GetMapping("/{id}")
    public ResponseEntity<LessonResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(lessonService.get(id));
    }

    @Operation(summary = "Update a lesson (TEACHER)")
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<LessonResponse> update(@PathVariable Long id,
                                                 @Valid @RequestBody LessonRequest request) {
        return ResponseEntity.ok(lessonService.update(id, request));
    }
}

