package com.forsaken.ecommerce.product.service;


import java.util.Map;

/**
 * Service interface for generating AWS S3 presigned URLs for upload and download operations.
 *
 * <p>These URLs allow clients to interact with S3 securely without exposing AWS credentials
 * or routing file content through the backend application.
 */
public interface IS3Service {

    /**
     * Generates a presigned URL that allows clients to upload a file directly to S3.
     *
     * <p>This method produces a temporary, secure URL and its associated S3 object key.
     * The client can then use the presigned URL to upload the file without requiring
     * AWS authentication headers.
     *
     * @param originalFilename the original name of the file being uploaded; must not be {@code null}
     * @param contentType      the MIME type of the file (e.g., image/png); must not be {@code null}
     * @return a map containing the generated S3 object key and the corresponding presigned upload URL
     */
    Map<String, String> generatePresignedUploadUrl(final String originalFilename, final String contentType);

    /**
     * Generates a presigned URL for downloading a file securely from S3.
     *
     * <p>The generated URL provides time-limited access to the specified object,
     * allowing clients to retrieve protected content without exposing AWS credentials.
     *
     * @param imageUrl the full S3 object key for the file; must not be {@code null}
     * @return a temporarily valid URL that can be used to download the object from S3
     */
    String generatePresignedDownloadUrl(final String imageUrl);
}
