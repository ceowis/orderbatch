package com.example.orderbatch;

import com.example.orderbatch.service.OrderBatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
@RequiredArgsConstructor
public class OrderBatchApplication implements CommandLineRunner {

    private final OrderBatchService orderBatchService;

    public static void main(String[] args) {
        SpringApplication.run(OrderBatchApplication.class, args);
    }

    /**
     * 애플리케이션 시작 후 자동으로 배치 실행 (데모용)
     * 운영 환경에서는 스케줄러(@Scheduled) 또는 REST API 트리거를 사용
     */
    @Override
    public void run(String... args) throws Exception {
        log.info("Starting order batch processing demo...");

        JobExecution execution = orderBatchService.runOrderProcessingJob();

        if (ExitStatus.COMPLETED.equals(execution.getExitStatus())) {
            log.info("✅ Batch completed successfully!");
        } else {
            log.error("❌ Batch failed with status: {}", execution.getExitStatus());
        }
    }
}
