package com.forsaken.ecommerce.product.exceptions;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CategoryNotFoundExceptions extends Exception {

    private final String message;
    private final String methodName;

    public CategoryNotFoundExceptions(final String message,
                                     final String methodName
    ) {
        super(String.format("%s in %s", message, methodName));
        this.message = message;
        this.methodName = methodName;
    }
}
