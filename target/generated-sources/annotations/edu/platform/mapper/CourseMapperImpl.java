package edu.platform.mapper;

import edu.platform.dto.request.CourseRequest;
import edu.platform.dto.response.CourseResponse;
import edu.platform.entity.Course;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-23T17:14:04+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.2 (Oracle Corporation)"
)
@Component
public class CourseMapperImpl implements CourseMapper {

    @Autowired
    private UserMapper userMapper;

    @Override
    public CourseResponse toResponse(Course course) {
        if ( course == null ) {
            return null;
        }

        CourseResponse courseResponse = new CourseResponse();

        courseResponse.setId( course.getId() );
        courseResponse.setTitle( course.getTitle() );
        courseResponse.setDescription( course.getDescription() );
        courseResponse.setTeacher( userMapper.toResponse( course.getTeacher() ) );
        courseResponse.setCreatedAt( course.getCreatedAt() );
        courseResponse.setUpdatedAt( course.getUpdatedAt() );

        return courseResponse;
    }

    @Override
    public List<CourseResponse> toResponseList(List<Course> courses) {
        if ( courses == null ) {
            return null;
        }

        List<CourseResponse> list = new ArrayList<CourseResponse>( courses.size() );
        for ( Course course : courses ) {
            list.add( toResponse( course ) );
        }

        return list;
    }

    @Override
    public Course toEntity(CourseRequest request) {
        if ( request == null ) {
            return null;
        }

        Course.CourseBuilder course = Course.builder();

        course.title( request.getTitle() );
        course.description( request.getDescription() );

        return course.build();
    }
}
