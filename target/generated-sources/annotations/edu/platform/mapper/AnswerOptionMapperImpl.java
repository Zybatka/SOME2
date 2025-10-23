package edu.platform.mapper;

import edu.platform.dto.request.AnswerOptionRequest;
import edu.platform.dto.response.AnswerOptionResponse;
import edu.platform.entity.AnswerOption;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-23T17:14:04+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.2 (Oracle Corporation)"
)
@Component
public class AnswerOptionMapperImpl implements AnswerOptionMapper {

    @Override
    public AnswerOptionResponse toResponse(AnswerOption answerOption) {
        if ( answerOption == null ) {
            return null;
        }

        AnswerOptionResponse answerOptionResponse = new AnswerOptionResponse();

        answerOptionResponse.setId( answerOption.getId() );
        answerOptionResponse.setOptionText( answerOption.getOptionText() );
        answerOptionResponse.setOrderIndex( answerOption.getOrderIndex() );

        return answerOptionResponse;
    }

    @Override
    public List<AnswerOptionResponse> toResponseList(List<AnswerOption> answerOptions) {
        if ( answerOptions == null ) {
            return null;
        }

        List<AnswerOptionResponse> list = new ArrayList<AnswerOptionResponse>( answerOptions.size() );
        for ( AnswerOption answerOption : answerOptions ) {
            list.add( toResponse( answerOption ) );
        }

        return list;
    }

    @Override
    public AnswerOption toEntity(AnswerOptionRequest request) {
        if ( request == null ) {
            return null;
        }

        AnswerOption.AnswerOptionBuilder answerOption = AnswerOption.builder();

        answerOption.optionText( request.getOptionText() );
        answerOption.isCorrect( request.getIsCorrect() );
        answerOption.orderIndex( request.getOrderIndex() );

        return answerOption.build();
    }
}
