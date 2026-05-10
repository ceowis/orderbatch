package com.example.orderbatch.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 주문 도메인 객체
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Order {

    private Long orderId;
    private Long customerId;
    private String customerName;
    private String productCode;
    private String productName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalAmount;
    private OrderStatus orderStatus;
    private LocalDateTime orderedAt;
    private LocalDateTime processedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public enum OrderStatus {
        PENDING,    // 처리 대기
        PROCESSED,  // 처리 완료
        FAILED,     // 처리 실패
        CANCELLED   // 취소
    }
}
