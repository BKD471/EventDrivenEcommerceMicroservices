package com.forsaken.ecommerce.order.product;


import com.forsaken.ecommerce.common.exceptions.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements IProductService{

    @Value("${application.config.product-url}")
    private String productUrl;
    private final RestTemplate restTemplate;

    @Override
    public CompletableFuture<List<PurchaseResponse>> purchaseProducts(final List<PurchaseRequest> requestBody) throws BusinessException {
        log.info("Product request received: {}", requestBody);
        final HttpHeaders headers = new HttpHeaders();
        headers.set(CONTENT_TYPE, APPLICATION_JSON_VALUE);

        final HttpEntity<List<PurchaseRequest>> requestEntity = new HttpEntity<>(requestBody, headers);
        final ParameterizedTypeReference<List<PurchaseResponse>> responseType = new ParameterizedTypeReference<>() {};
        final ResponseEntity<List<PurchaseResponse>> responseEntity = restTemplate.exchange(
                productUrl + "/purchase",
                POST,
                requestEntity,
                responseType
        );

        if (responseEntity.getStatusCode().isError()) {
            log.error("Product request failed: {}", responseEntity.getBody());
            throw new BusinessException(
                    "An error occurred while processing the products purchase: " + responseEntity.getStatusCode(),
                    "purchaseProducts(final List<PurchaseRequest> requestBody)"
            );
        }
        return CompletableFuture.supplyAsync(responseEntity::getBody);
    }
}
