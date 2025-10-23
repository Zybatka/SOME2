package edu.platform.mapper;

import edu.platform.dto.response.LessonResponse;
import edu.platform.entity.Lesson;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-23T17:14:04+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.2 (Oracle Corporation)"
)
@Component
public class LessonMapperImpl implements LessonMapper {

    @Autowired
    private CourseMapper courseMapper;

    @Override
    public LessonResponse toResponse(Lesson lesson) {
        if ( lesson == null ) {
            return null;
        }

        LessonResponse lessonResponse = new LessonResponse();

        lessonResponse.setId( lesson.getId() );
        lessonResponse.setCourse( courseMapper.toResponse( lesson.getCourse() ) );
        lessonResponse.setTitle( lesson.getTitle() );
        lessonResponse.setContent( lesson.getContent() );
        lessonResponse.setOrderIndex( lesson.getOrderIndex() );
        lessonResponse.setVersion( lesson.getVersion() );
        lessonResponse.setCreatedAt( lesson.getCreatedAt() );
        lessonResponse.setUpdatedAt( lesson.getUpdatedAt() );

        return lessonResponse;
    }
}
