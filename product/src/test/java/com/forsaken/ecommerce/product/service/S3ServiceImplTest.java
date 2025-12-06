package com.forsaken.ecommerce.product.service;


import com.forsaken.ecommerce.product.configs.s3.S3Properties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.URL;
import java.util.Map;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class S3ServiceImplTest {

    @Mock
    private S3Properties s3Properties;

    @Mock
    private S3Presigner presigner;

    @Mock
    private PresignedPutObjectRequest presignedPutObjectRequest;

    @Mock
    private PresignedGetObjectRequest presignedGetObjectRequest;

    private S3ServiceImpl s3Service;

    @BeforeEach
    void setup() {
        s3Service = new S3ServiceImpl(s3Properties, presigner);
    }

    @Test
    void generatePresignedUploadUrl_ShouldReturnUrlAndKey() throws Exception {
        // Given
        when(s3Properties.bucketName()).thenReturn("test-bucket");
        when(s3Properties.expiration()).thenReturn(30L);
        final URL fakeUrl = new URL("https://s3.com/upload-url");
        // Mock the URL returned by AWS Presigned request
        lenient().when(presignedPutObjectRequest.url())
                .thenReturn(fakeUrl);
        // Mock Consumer< GetObjectPresignRequest.Builder >
        lenient().when(presigner.presignPutObject(any(Consumer.class)))
                .thenAnswer(inv -> {
                    PutObjectPresignRequest.Builder builder =
                            PutObjectPresignRequest.builder();
                    Consumer<PutObjectPresignRequest.Builder> consumer =
                            inv.getArgument(0);
                    consumer.accept(builder);
                    return presignedPutObjectRequest;
                });

        // When
        final Map<String, String> result =
                s3Service.generatePresignedUploadUrl("photo.png", "image/png");

        // Then
        assertEquals("https://s3.com/upload-url", result.get("uploadUrl"));
        assertTrue(result.get("key").contains("uploads/"));
        assertTrue(result.get("key").endsWith("_photo.png"));
    }

    @Test
    void generatePresignedUploadUrl_ShouldThrowIllegalState_WhenPresignerReturnsNull() {
        // Given
        when(s3Properties.bucketName()).thenReturn("test-bucket");
        when(s3Properties.expiration()).thenReturn(30L);
        // Simulate AWS returning null instead of a PresignedPutObjectRequest
        lenient().when(presigner.presignPutObject(any(Consumer.class)))
                .thenReturn(null);

        // When
        final IllegalStateException exception =
                assertThrows(IllegalStateException.class, () ->
                        s3Service.generatePresignedUploadUrl("photo.png", "image/png")
                );

        // Then
        assertEquals("Unable to generate presigned upload URL", exception.getMessage());
    }


    @Test
    void generatePresignedUploadUrl_ShouldThrowIllegalState_WhenPresignerReturnsUrlNull() {
        // Given
        when(s3Properties.bucketName()).thenReturn("test-bucket");
        when(s3Properties.expiration()).thenReturn(30L);
        final String imageUrl = "https://cdn.com/uploads/img.png";
        // Simulate AWS returning presigned url null
        lenient().when(presigner.presignPutObject(any(Consumer.class)))
                .thenAnswer(inv -> {
                    PutObjectPresignRequest.Builder builder =
                            PutObjectPresignRequest.builder();
                    Consumer<PutObjectPresignRequest.Builder> consumer =
                            inv.getArgument(0);
                    consumer.accept(builder);
                    return presignedPutObjectRequest;
                });
        lenient().when(presignedPutObjectRequest.url())
                .thenReturn(null);

        // When
        final IllegalStateException exception =
                assertThrows(IllegalStateException.class, () ->
                        s3Service.generatePresignedUploadUrl(imageUrl,"image/png")
                );

        // Then
        assertEquals("Unable to generate presigned upload URL", exception.getMessage());
    }

    @Test
    void generatePresignedDownloadUrl_ShouldReturnPresignedUrl() throws Exception {
        // Given
        when(s3Properties.bucketName()).thenReturn("test-bucket");
        when(s3Properties.expiration()).thenReturn(30L);
        final String imageUrl = "https://cdn.com/uploads/img_123.png";
        final URL fakeDownloadUrl = new URL("https://s3.com/download-url");
        // Mock the URL returned by AWS Presigned request
        lenient().when(presignedGetObjectRequest.url())
                .thenReturn(fakeDownloadUrl);
        // Mock Consumer<GetObjectPresignRequest.Builder>
        lenient().when(presigner.presignGetObject(any(Consumer.class)))
                .thenAnswer(inv -> {
                    GetObjectPresignRequest.Builder builder =
                            GetObjectPresignRequest.builder();
                    Consumer<GetObjectPresignRequest.Builder> consumer =
                            inv.getArgument(0);
                    consumer.accept(builder); // simulate AWS builder population
                    return presignedGetObjectRequest;
                });

        // When
        final String resultUrl = s3Service.generatePresignedDownloadUrl(imageUrl);
        // Then
        assertEquals("https://s3.com/download-url", resultUrl);
    }

    @Test
    void generatePresignedDownloadUrl_ShouldThrowIllegalState_WhenPresignerReturnsNull() {
        // Given
        when(s3Properties.bucketName()).thenReturn("test-bucket");
        when(s3Properties.expiration()).thenReturn(30L);
        final String imageUrl = "https://cdn.com/uploads/img.png";
        // Simulate AWS returning null
        lenient().when(presigner.presignGetObject(any(Consumer.class)))
                .thenReturn(null);

        // When
        final IllegalStateException exception =
                assertThrows(IllegalStateException.class, () ->
                        s3Service.generatePresignedDownloadUrl(imageUrl)
                );

        // Then
        assertEquals("Unable to generate presigned download URL", exception.getMessage());
    }

    @Test
    void generatePresignedDownloadUrl_ShouldThrowIllegalState_WhenPresignerReturnsUrlNull() {
        // Given
        when(s3Properties.bucketName()).thenReturn("test-bucket");
        when(s3Properties.expiration()).thenReturn(30L);
        final String imageUrl = "https://cdn.com/uploads/img.png";
        // Simulate AWS returning presigned url null
        lenient().when(presigner.presignGetObject(any(Consumer.class)))
                .thenAnswer(inv -> {
                    GetObjectPresignRequest.Builder builder =
                            GetObjectPresignRequest.builder();
                    Consumer<GetObjectPresignRequest.Builder> consumer =
                            inv.getArgument(0);
                    consumer.accept(builder); // simulate AWS builder population
                    return presignedGetObjectRequest;
                });
        lenient().when(presignedGetObjectRequest.url())
                .thenReturn(null);

        // When
        final IllegalStateException exception =
                assertThrows(IllegalStateException.class, () ->
                        s3Service.generatePresignedDownloadUrl(imageUrl)
                );

        // Then
        assertEquals("Unable to generate presigned download URL", exception.getMessage());
    }
}
