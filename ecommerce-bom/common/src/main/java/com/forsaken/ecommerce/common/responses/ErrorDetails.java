package com.forsaken.ecommerce.common.responses;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ErrorDetails(
        LocalDateTime timeStamp,
        String message,
        String details
) {
}
