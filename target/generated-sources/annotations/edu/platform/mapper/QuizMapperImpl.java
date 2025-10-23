package edu.platform.mapper;

import edu.platform.dto.request.QuizRequest;
import edu.platform.dto.response.QuizResponse;
import edu.platform.entity.Quiz;
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
public class QuizMapperImpl implements QuizMapper {

    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private QuestionMapper questionMapper;

    @Override
    public QuizResponse toResponse(Quiz quiz) {
        if ( quiz == null ) {
            return null;
        }

        QuizResponse quizResponse = new QuizResponse();

        quizResponse.setId( quiz.getId() );
        quizResponse.setTitle( quiz.getTitle() );
        quizResponse.setDescription( quiz.getDescription() );
        quizResponse.setCourse( courseMapper.toResponse( quiz.getCourse() ) );
        quizResponse.setMaxAttempts( quiz.getMaxAttempts() );
        quizResponse.setTimeLimitMinutes( quiz.getTimeLimitMinutes() );
        quizResponse.setIsActive( quiz.getIsActive() );
        quizResponse.setCreatedAt( quiz.getCreatedAt() );
        quizResponse.setUpdatedAt( quiz.getUpdatedAt() );
        quizResponse.setQuestions( questionMapper.toResponseList( quiz.getQuestions() ) );

        return quizResponse;
    }

    @Override
    public List<QuizResponse> toResponseList(List<Quiz> quizzes) {
        if ( quizzes == null ) {
            return null;
        }

        List<QuizResponse> list = new ArrayList<QuizResponse>( quizzes.size() );
        for ( Quiz quiz : quizzes ) {
            list.add( toResponse( quiz ) );
        }

        return list;
    }

    @Override
    public Quiz toEntity(QuizRequest request) {
        if ( request == null ) {
            return null;
        }

        Quiz.QuizBuilder quiz = Quiz.builder();

        quiz.title( request.getTitle() );
        quiz.description( request.getDescription() );
        quiz.maxAttempts( request.getMaxAttempts() );
        quiz.timeLimitMinutes( request.getTimeLimitMinutes() );

        return quiz.build();
    }
}
