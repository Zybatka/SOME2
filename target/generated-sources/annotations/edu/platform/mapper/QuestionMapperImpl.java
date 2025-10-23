package edu.platform.mapper;

import edu.platform.dto.request.QuestionRequest;
import edu.platform.dto.response.QuestionResponse;
import edu.platform.entity.Question;
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
public class QuestionMapperImpl implements QuestionMapper {

    @Autowired
    private AnswerOptionMapper answerOptionMapper;

    @Override
    public QuestionResponse toResponse(Question question) {
        if ( question == null ) {
            return null;
        }

        QuestionResponse questionResponse = new QuestionResponse();

        questionResponse.setId( question.getId() );
        questionResponse.setQuestionText( question.getQuestionText() );
        questionResponse.setQuestionType( question.getQuestionType() );
        questionResponse.setPoints( question.getPoints() );
        questionResponse.setOrderIndex( question.getOrderIndex() );
        questionResponse.setAnswerOptions( answerOptionMapper.toResponseList( question.getAnswerOptions() ) );

        return questionResponse;
    }

    @Override
    public List<QuestionResponse> toResponseList(List<Question> questions) {
        if ( questions == null ) {
            return null;
        }

        List<QuestionResponse> list = new ArrayList<QuestionResponse>( questions.size() );
        for ( Question question : questions ) {
            list.add( toResponse( question ) );
        }

        return list;
    }

    @Override
    public Question toEntity(QuestionRequest request) {
        if ( request == null ) {
            return null;
        }

        Question.QuestionBuilder question = Question.builder();

        question.questionText( request.getQuestionText() );
        question.questionType( request.getQuestionType() );
        question.points( request.getPoints() );
        question.orderIndex( request.getOrderIndex() );

        return question.build();
    }
}
