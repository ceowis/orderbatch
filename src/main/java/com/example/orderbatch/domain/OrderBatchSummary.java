package com.example.orderbatch.domain;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 배치 처리 집계 결과
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderBatchSummary {

    private Long summaryId;
    private Long jobExecutionId;
    private LocalDate batchDate;
    private int totalCount;
    private int successCount;
    private int failCount;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
}
