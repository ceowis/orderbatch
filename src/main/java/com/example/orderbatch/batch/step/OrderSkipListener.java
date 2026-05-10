package com.example.orderbatch.batch.step;

import com.example.orderbatch.domain.Order;
import com.example.orderbatch.domain.OrderErrorLog;
import com.example.orderbatch.mapper.OrderBatchMapper;
import com.example.orderbatch.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.SkipListener;
import org.springframework.stereotype.Component;

/**
 * Skip 이벤트 리스너
 * - 처리 실패 시 오류 로그 저장 및 주문 상태 FAILED 처리
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OrderSkipListener implements SkipListener<Order, Order> {

    private final OrderMapper orderMapper;
    private final OrderBatchMapper orderBatchMapper;

    @Override
    public void onSkipInRead(Throwable t) {
        log.error("Skip on READ: {}", t.getMessage());
    }

    @Override
    public void onSkipInProcess(Order item, Throwable t) {
        log.error("Skip on PROCESS: orderId={}, error={}", item.getOrderId(), t.getMessage());

        // 주문 상태를 FAILED로 변경
        orderMapper.updateOrderFailed(item.getOrderId());

        // 에러 로그 저장
        orderBatchMapper.insertErrorLog(OrderErrorLog.builder()
                .orderId(item.getOrderId())
                .errorMessage(t.getMessage())
                .stepName("orderProcessingStep")
                .build());
    }

    @Override
    public void onSkipInWrite(Order item, Throwable t) {
        log.error("Skip on WRITE: orderId={}, error={}", item.getOrderId(), t.getMessage());

        orderBatchMapper.insertErrorLog(OrderErrorLog.builder()
                .orderId(item.getOrderId())
                .errorMessage("Write failed: " + t.getMessage())
                .stepName("orderProcessingStep")
                .build());
    }
}
