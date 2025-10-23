package edu.platform.mapper;

import edu.platform.dto.response.MediaAssetResponse;
import edu.platform.entity.MediaAsset;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-23T17:14:04+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.2 (Oracle Corporation)"
)
@Component
public class MediaAssetMapperImpl implements MediaAssetMapper {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CourseMapper courseMapper;

    @Override
    public MediaAssetResponse toResponse(MediaAsset entity) {
        if ( entity == null ) {
            return null;
        }

        MediaAssetResponse mediaAssetResponse = new MediaAssetResponse();

        mediaAssetResponse.setId( entity.getId() );
        mediaAssetResponse.setOwner( userMapper.toResponse( entity.getOwner() ) );
        mediaAssetResponse.setCourse( courseMapper.toResponse( entity.getCourse() ) );
        mediaAssetResponse.setFileName( entity.getFileName() );
        mediaAssetResponse.setFilePath( entity.getFilePath() );
        mediaAssetResponse.setFileSize( entity.getFileSize() );
        mediaAssetResponse.setContentType( entity.getContentType() );
        mediaAssetResponse.setChecksum( entity.getChecksum() );
        mediaAssetResponse.setCreatedAt( entity.getCreatedAt() );

        return mediaAssetResponse;
    }
}
