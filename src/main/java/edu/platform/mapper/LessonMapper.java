package edu.platform.mapper;

import edu.platform.dto.response.LessonResponse;
import edu.platform.entity.Lesson;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CourseMapper.class})
public interface LessonMapper {
    LessonResponse toResponse(Lesson lesson);
}

