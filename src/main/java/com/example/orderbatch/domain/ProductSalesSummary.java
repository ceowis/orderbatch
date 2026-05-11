package com.example.orderbatch.domain;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductSalesSummary {
    private Long summaryId; // 집계 ID
    private Long jobExecutionId; // 배치 실행 ID
    private String productCode; // 상품 코드
    private String productName; // 상품명
    private int totalQuantity; // 총 판매 수량
    private double totalRevenue; // 총 판매 금액
    private int orderCount; // 주문 건수
    private Date batchDate; // 배치 실행 날짜
}
