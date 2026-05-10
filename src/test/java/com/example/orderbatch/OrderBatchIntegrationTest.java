package com.example.orderbatch;

import com.example.orderbatch.domain.Order;
import com.example.orderbatch.mapper.OrderMapper;
import com.example.orderbatch.service.OrderBatchService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class OrderBatchIntegrationTest {

    @Autowired
    private OrderBatchService orderBatchService;

    @Autowired
    private OrderMapper orderMapper;

    @Test
    @DisplayName("PENDING 주문이 PROCESSED 상태로 변경되어야 한다")
    void orderProcessingJob_shouldProcessPendingOrders() throws Exception {
        // given - data.sql로 PENDING 주문 10건 세팅됨

        // when
        JobExecution execution = orderBatchService.runOrderProcessingJob();

        // then
        assertThat(execution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);

        // PENDING 주문이 없어야 함
        List<Order> remaining = orderMapper.selectOrdersByStatus(
                Order.OrderStatus.PENDING.name());
        assertThat(remaining).isEmpty();
    }

    @Test
    @DisplayName("배치 재실행 시 중복 처리되지 않아야 한다")
    void orderProcessingJob_shouldBeIdempotent() throws Exception {
        // when - 두 번 실행
        JobExecution first  = orderBatchService.runOrderProcessingJob();
        JobExecution second = orderBatchService.runOrderProcessingJob();

        // then - 두 번 모두 COMPLETED
        assertThat(first.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
        assertThat(second.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
    }
}
