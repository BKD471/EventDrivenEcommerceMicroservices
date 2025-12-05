package com.forsaken.ecommerce.product.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class S3ServiceImpl implements IS3Service {


    @Override
    public Map<String, String> generatePresignedUploadUrl(
            final String originalFilename,
            final String contentType
    ) {
        log.info("Generate Presigned Upload URL for file: {}", originalFilename);
        final String key = "uploads/" + UUID.randomUUID() + "_" + originalFilename;

        final PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket("// TODO give a Bucket Name") // TODO pass down a S3 bucket name
                .key(key)
                .contentType(contentType)
                .build();

        final PresignedPutObjectRequest presignedRequest = null; // TODO Create a presigned put object request with expiration
        log.info("Presigned Upload URL: {}", presignedRequest.url());

        return Map.of(
                "uploadUrl", presignedRequest.url().toString(),
                "key", key   // 👈 include this too!
        );
    }

    @Override
    public String generatePresignedDownloadUrl(final String imageUrl) {
        log.info("Generate Presigned Download URL for image: {}", imageUrl);
        final String key = imageUrl.substring(imageUrl.lastIndexOf("/uploads") + 1);
        final GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket("// TODO give a Bucket Name") // TODO pass down a S3 bucket name
                .key(key)
                .build();

        final PresignedGetObjectRequest presignedGet = null; // TODO Create a presigned put object request
        log.info("Presigned Download URL: {}", presignedGet.url());
        return presignedGet.url().toString();
    }
}
