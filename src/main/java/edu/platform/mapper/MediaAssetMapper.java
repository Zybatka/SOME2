package edu.platform.mapper;

import edu.platform.dto.response.MediaAssetResponse;
import edu.platform.entity.MediaAsset;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class, CourseMapper.class})
public interface MediaAssetMapper {
    MediaAssetResponse toResponse(MediaAsset entity);
}

