package com.forsaken.ecommerce.common.responses;


import lombok.Builder;

@Builder
public record ApiResponse<T>(
        Status status,
        T data,
        String message
) {

    public enum Status {
        SUCCESS,FAILED,ERROR
    }
}
