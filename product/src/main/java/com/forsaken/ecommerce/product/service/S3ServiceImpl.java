package com.forsaken.ecommerce.product.service;


import com.forsaken.ecommerce.product.configs.s3.S3Properties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class S3ServiceImpl implements IS3Service {

    private final S3Properties s3Properties;
    private final S3Presigner presigner;

    @Override
    public Map<String, String> generatePresignedUploadUrl(
            final String originalFilename,
            final String contentType
    ) {
        log.info("Generate Presigned Upload URL for file: {}", originalFilename);
        final String key = "uploads/" + UUID.randomUUID() + "_" + originalFilename;

        final PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(s3Properties.bucketName())
                .key(key)
                .contentType(contentType)
                .build();

        final PresignedPutObjectRequest presignedRequest = presigner.presignPutObject(builder ->
                builder.signatureDuration(Duration.ofMinutes(s3Properties.expiration())) // URL valid for 30 mins
                        .putObjectRequest(putObjectRequest)
        );

        if (presignedRequest == null || presignedRequest.url() == null) {
            throw new IllegalStateException("Unable to generate presigned upload URL");
        }

        log.info("Presigned Upload URL: {}", presignedRequest.url());
        return Map.of(
                "uploadUrl", presignedRequest.url().toString(),
                "key", key
        );
    }

    @Override
    public String generatePresignedDownloadUrl(final String imageUrl) {
        log.info("Generate Presigned Download URL for image: {}", imageUrl);
        final String key = imageUrl.substring(imageUrl.lastIndexOf("/uploads") + 1);
        final GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(s3Properties.bucketName())
                .key(key)
                .build();

        final PresignedGetObjectRequest presignedGet = presigner.presignGetObject(builder ->
                builder.signatureDuration(Duration.ofMinutes(s3Properties.expiration()))
                        .getObjectRequest(getObjectRequest)
        );

        if (presignedGet == null || presignedGet.url() == null) {
            throw new IllegalStateException("Unable to generate presigned download URL");
        }

        log.info("Presigned Download URL: {}", presignedGet.url());
        return presignedGet.url().toString();
    }
}
