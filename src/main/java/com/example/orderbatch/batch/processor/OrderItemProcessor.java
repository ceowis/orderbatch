package com.example.orderbatch.batch.processor;

import com.example.orderbatch.domain.Order;
import com.example.orderbatch.domain.Order.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 주문 처리 Processor
 * - 유효성 검사
 * - 비즈니스 로직 적용 (할인 계산, 검증 등)
 * - null 반환 시 해당 아이템은 Writer에서 스킵됨
 */
@Slf4j
@Component
public class OrderItemProcessor implements ItemProcessor<Order, Order> {

    private static final BigDecimal MAX_ORDER_AMOUNT = new BigDecimal("10000000"); // 1천만원 한도
    private static final BigDecimal MIN_ORDER_AMOUNT = BigDecimal.ONE;

    @Override
    public Order process(Order order) throws Exception {
        log.info("읽어온 데이터");
        log.debug("Processing order: orderId={}, customer={}, amount={}, 주문상태={}",
                order.getOrderId(), order.getCustomerName(), order.getTotalAmount(), order.getOrderStatus());

        // 1. 이미 처리된 주문은 스킵
        if (order.getOrderStatus() != OrderStatus.PENDING) {
            log.info("##### Skip non-PENDING order: orderId={}, status={}",
                    order.getOrderId(), order.getOrderStatus());
            return null;
        }

        // 2. 주문 금액 유효성 검사
        validateOrderAmount(order);

        // 3. 총 금액 재계산 (quantity * unitPrice)
        BigDecimal recalculated = order.getUnitPrice()
                .multiply(BigDecimal.valueOf(order.getQuantity()));

        if (recalculated.compareTo(order.getTotalAmount()) != 0) {
            log.warn("Total amount mismatch for orderId={}: stored={}, recalculated={}",
                    order.getOrderId(), order.getTotalAmount(), recalculated);
            // 재계산 값으로 보정한 새 객체 반환
            return Order.builder()
                    .orderId(order.getOrderId())
                    .customerId(order.getCustomerId())
                    .customerName(order.getCustomerName())
                    .productCode(order.getProductCode())
                    .productName(order.getProductName())
                    .quantity(order.getQuantity())
                    .unitPrice(order.getUnitPrice())
                    .totalAmount(recalculated)          // 보정된 금액
                    .orderStatus(OrderStatus.PENDING)
                    .orderedAt(order.getOrderedAt())
                    .build();
        }

        log.debug("Order validated successfully: orderId={}", order.getOrderId());
        return order;
    }

    private void validateOrderAmount(Order order) {
        if (order.getTotalAmount() == null) {
            throw new IllegalArgumentException(
                    "Total amount is null for orderId=" + order.getOrderId());
        }
        if (order.getTotalAmount().compareTo(MIN_ORDER_AMOUNT) < 0) {
            throw new IllegalArgumentException(
                    "Total amount is too small: " + order.getTotalAmount());
        }
        if (order.getTotalAmount().compareTo(MAX_ORDER_AMOUNT) > 0) {
            throw new IllegalArgumentException(
                    "Total amount exceeds limit: " + order.getTotalAmount());
        }
        if (order.getQuantity() == null || order.getQuantity() <= 0) {
            throw new IllegalArgumentException(
                    "Invalid quantity for orderId=" + order.getOrderId());
        }
    }
}
