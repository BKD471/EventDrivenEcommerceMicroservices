package com.forsaken.ecommerce.product.service;


import java.util.Map;

public interface IS3Service {

    /**
     * this service creates Product in database.
     *
     * @param originalFilename - name of file to upload
     * @param contentType      - type of file
     * @return Map<String,String>  - Map of uploadedUrl with Key.
     */
    Map<String, String> generatePresignedUploadUrl(final String originalFilename, final String contentType);

    /**
     * this service creates Product in database.
     *
     * @param imageUrl - name of image file in S3
     * @return String  - url of image in S3 with expiration applied..
     */
    String generatePresignedDownloadUrl(final String imageUrl);
}
