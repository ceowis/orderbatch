package com.example.orderbatch.batch.processor;

import com.example.orderbatch.domain.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
public class ProductOrderItemProcessor implements ItemProcessor<Order, Order> {

    private static final BigDecimal MAX_ORDER_AMOUNT = new BigDecimal("10000000"); // 1천만원 한도
    private static final BigDecimal MIN_ORDER_AMOUNT = BigDecimal.ONE;

    @Override
    public Order process(Order order) throws Exception {
        log.info("ProductOrderItemProcessor: Processing product order: orderId={}, productCode={}, productName={}",
                order.getOrderId(), order.getProductCode(), order.getProductName());

        // 1. 이미 처리된 주문은 null을 리턴하여 writer에서 스킵
        if (order.getOrderStatus() != Order.OrderStatus.PENDING) {
            log.info("##### Skip non-PENDING order: orderId={}, status={}",
                    order.getOrderId(), order.getOrderStatus());
            return null;
        }

        // 2. 주문 금액 유효성 검사
        validateOrderAmount(order);

        // 3.




//        // 상품명에 "상품"이 포함되어 있지 않으면 스킵
//        if (order.getProductName() == null || !order.getProductName().contains("상품")) {
//            log.info("##### Skip non-product order: orderId={}, productName={}",
//                    order.getOrderId(), order.getProductName());
//            return null; // null 반환 시 Writer에서 해당 아이템은 스킵됨
//        }

        // 상품명에 "상품"이 포함된 경우 그대로 반환
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
