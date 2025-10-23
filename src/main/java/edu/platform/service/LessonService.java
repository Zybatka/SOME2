package edu.platform.service;

import edu.platform.dto.request.LessonRequest;
import edu.platform.dto.response.LessonResponse;
import edu.platform.entity.Course;
import edu.platform.entity.Lesson;
import edu.platform.exception.ResourceNotFoundException;
import edu.platform.mapper.LessonMapper;
import edu.platform.repository.CourseRepository;
import edu.platform.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;
    private final LessonMapper lessonMapper;

    @Transactional
    public LessonResponse create(LessonRequest request) {
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found: " + request.getCourseId()));

        Lesson lesson = Lesson.builder()
                .course(course)
                .title(request.getTitle())
                .content(request.getContent())
                .orderIndex(request.getOrderIndex())
                .build();

        return lessonMapper.toResponse(lessonRepository.save(lesson));
    }

    @Transactional(readOnly = true)
    public LessonResponse get(Long id) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found: " + id));
        return lessonMapper.toResponse(lesson);
    }

    @Transactional
    public LessonResponse update(Long id, LessonRequest request) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found: " + id));

        if (!lesson.getCourse().getId().equals(request.getCourseId())) {
            Course course = courseRepository.findById(request.getCourseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Course not found: " + request.getCourseId()));
            lesson.setCourse(course);
        }
        lesson.setTitle(request.getTitle());
        lesson.setContent(request.getContent());
        lesson.setOrderIndex(request.getOrderIndex());

        return lessonMapper.toResponse(lessonRepository.save(lesson));
    }
}

