package edu.platform.mapper;

import edu.platform.dto.response.QuizAttemptResponse;
import edu.platform.entity.QuizAttempt;
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
public class QuizAttemptMapperImpl implements QuizAttemptMapper {

    @Autowired
    private QuizMapper quizMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public QuizAttemptResponse toResponse(QuizAttempt quizAttempt) {
        if ( quizAttempt == null ) {
            return null;
        }

        QuizAttemptResponse quizAttemptResponse = new QuizAttemptResponse();

        quizAttemptResponse.setId( quizAttempt.getId() );
        quizAttemptResponse.setQuiz( quizMapper.toResponse( quizAttempt.getQuiz() ) );
        quizAttemptResponse.setStudent( userMapper.toResponse( quizAttempt.getStudent() ) );
        quizAttemptResponse.setAttemptNumber( quizAttempt.getAttemptNumber() );
        quizAttemptResponse.setScore( quizAttempt.getScore() );
        quizAttemptResponse.setMaxScore( quizAttempt.getMaxScore() );
        quizAttemptResponse.setStartedAt( quizAttempt.getStartedAt() );
        quizAttemptResponse.setCompletedAt( quizAttempt.getCompletedAt() );
        quizAttemptResponse.setIsCompleted( quizAttempt.getIsCompleted() );

        return quizAttemptResponse;
    }

    @Override
    public List<QuizAttemptResponse> toResponseList(List<QuizAttempt> quizAttempts) {
        if ( quizAttempts == null ) {
            return null;
        }

        List<QuizAttemptResponse> list = new ArrayList<QuizAttemptResponse>( quizAttempts.size() );
        for ( QuizAttempt quizAttempt : quizAttempts ) {
            list.add( toResponse( quizAttempt ) );
        }

        return list;
    }
}
