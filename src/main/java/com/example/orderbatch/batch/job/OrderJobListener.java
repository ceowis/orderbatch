package com.example.orderbatch.batch.job;

import com.example.orderbatch.domain.OrderBatchSummary;
import com.example.orderbatch.mapper.OrderBatchMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Job 실행 전후 처리 리스너
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OrderJobListener implements JobExecutionListener {

    private final OrderBatchMapper orderBatchMapper;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("========================================");
        log.info("=====================================   01");
        log.info("  Order Batch Job STARTED");
        log.info("  Job Name     : {}", jobExecution.getJobInstance().getJobName());
        log.info("  Execution ID : {}", jobExecution.getId());
        log.info("  Start Time   : {}", jobExecution.getStartTime());
        log.info("========================================");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        log.info("========================================");
        log.info("  Order Batch Job FINISHED");
        log.info("  Status       : {}", jobExecution.getStatus());
        log.info("  End Time     : {}", jobExecution.getEndTime());

        // 스텝 실행 결과 집계
        int totalRead    = 0;
        int totalWrite   = 0;
        int totalSkip    = 0;
        int totalProcess = 0;

        for (var stepExec : jobExecution.getStepExecutions()) {
            totalRead    += stepExec.getReadCount();
            totalWrite   += stepExec.getWriteCount();
            totalSkip    += stepExec.getProcessSkipCount() + stepExec.getWriteSkipCount();
            totalProcess += stepExec.getFilterCount();

            log.info("  [Step: {}] read={}, write={}, skip={}, filter={}",
                    stepExec.getStepName(),
                    stepExec.getReadCount(),
                    stepExec.getWriteCount(),
                    stepExec.getProcessSkipCount() + stepExec.getWriteSkipCount(),
                    stepExec.getFilterCount());
        }

        log.info("  Summary: read={}, write={}, skip={}", totalRead, totalWrite, totalSkip);
        log.info("========================================");

        // 집계 결과 DB 저장
        try {
            OrderBatchSummary summary = OrderBatchSummary.builder()
                    .jobExecutionId(jobExecution.getId()) // JobExecution ID 저장
                    .batchDate(LocalDate.now())
                    .totalCount(totalRead)
                    .successCount(totalWrite)
                    .failCount(totalSkip)
                    .totalAmount(BigDecimal.ZERO)  // 필요 시 StepContext에서 집계
                    .build();

            orderBatchMapper.insertBatchSummary(summary);
            log.info("Batch summary saved: {}", summary);
        } catch (Exception e) {
            log.error("Failed to save batch summary", e);
        }
    }
}
