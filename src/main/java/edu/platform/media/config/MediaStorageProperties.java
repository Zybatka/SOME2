package edu.platform.media.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "media.storage")
public class MediaStorageProperties {
    private String baseDir = "storage/media";
    private List<String> allowedContentTypes;
}

