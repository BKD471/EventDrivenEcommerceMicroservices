package com.forsaken.ecommerce.common.exceptions;


import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BusinessException extends Exception {

    private final String message;
    private final String methodName;

    public BusinessException(
            final String message,
            final String methodName
    ) {
        super(String.format("%s in %s", message, methodName));
        this.message = message;
        this.methodName = methodName;
    }
}
