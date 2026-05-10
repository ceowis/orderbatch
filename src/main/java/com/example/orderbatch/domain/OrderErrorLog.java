package com.example.orderbatch.domain;

import lombok.*;
import java.time.LocalDateTime;

/**
 * 배치 처리 오류 로그
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderErrorLog {

    private Long errorId;
    private Long orderId;
    private String errorMessage;
    private String stepName;
    private LocalDateTime occurredAt;
}
