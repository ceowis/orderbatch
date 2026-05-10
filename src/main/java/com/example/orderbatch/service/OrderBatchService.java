package com.example.orderbatch.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 배치 Job 실행 서비스
 * - 스케줄러 또는 API로부터 Job을 트리거
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderBatchService {

    private final JobLauncher jobLauncher;
    private final Job         orderProcessingJob;

    /**
     * 주문 처리 배치 실행
     */
    public JobExecution runOrderProcessingJob() throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addString("runAt", LocalDateTime.now().toString())  // 매 실행 고유 파라미터
                .toJobParameters();

        log.info("Launching orderProcessingJob with params: {}", params);

        JobExecution execution = jobLauncher.run(orderProcessingJob, params);

        log.info("Job finished with status: {}", execution.getStatus());
        return execution;
    }
}
