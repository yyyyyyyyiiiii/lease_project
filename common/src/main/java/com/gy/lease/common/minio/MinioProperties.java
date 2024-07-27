package com.gy.lease.common.minio;


import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "minio")
@ConditionalOnProperty(name = "minio.endpoint")
public class MinioProperties {

    private String endpoint;
    private String accessKey;
    private String secretKey;

    private String bucketName;
}
