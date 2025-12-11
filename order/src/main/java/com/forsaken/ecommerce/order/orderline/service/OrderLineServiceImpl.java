package com.forsaken.ecommerce.order.orderline.service;

import com.forsaken.ecommerce.order.orderline.dto.OrderLineRequest;
import com.forsaken.ecommerce.order.orderline.dto.OrderLineResponse;
import com.forsaken.ecommerce.order.orderline.model.OrderLine;
import com.forsaken.ecommerce.order.orderline.repository.OrderLineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderLineServiceImpl implements IOrderLineService {

    private final OrderLineRepository orderLineRepository;

    @Override
    public void saveOrderLine(final OrderLineRequest request) {
        log.info("Save Order Line Request: {}", request);
        orderLineRepository.save(request.toOrderLine());
    }

    @Override
    public List<OrderLineResponse> findAllByOrderId(final Integer orderId) {
        log.info("Find all Order Lines By Order Id: {}", orderId);
        return orderLineRepository.findAllByOrderId(orderId)
                .stream()
                .map(OrderLine::toOrderLineResponse)
                .collect(Collectors.toList());
    }
}
