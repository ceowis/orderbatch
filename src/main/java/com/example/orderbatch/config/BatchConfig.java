package com.example.orderbatch.config;

import com.example.orderbatch.batch.job.OrderJobListener;
import com.example.orderbatch.batch.processor.OrderItemProcessor;
import com.example.orderbatch.batch.processor.ProductOrderItemProcessor;
import com.example.orderbatch.batch.step.OrderSkipListener;
import com.example.orderbatch.batch.writer.OrderItemWriter;
import com.example.orderbatch.domain.Order;
import com.example.orderbatch.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisCursorItemReader;
import org.mybatis.spring.batch.builder.MyBatisCursorItemReaderBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Map;

/**
 * Spring Batch Job 및 Step 설정
 *
 * Job 구성:
 *   orderProcessingJob
 *     └─ orderProcessingStep  (MyBatisCursorItemReader → OrderItemProcessor → OrderItemWriter)
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class BatchConfig {

    private final SqlSessionFactory sqlSessionFactory;
    private final OrderItemProcessor orderItemProcessor;
    private final ProductOrderItemProcessor productOrderItemProcessor;
    private final OrderItemWriter    orderItemWriter;
    private final OrderJobListener   orderJobListener;
    private final OrderSkipListener  orderSkipListener;

    @Value("${batch.chunk-size:10}")
    private int chunkSize;

    /* =========================================================
     *  Reader - MyBatisCursorItemReader (대용량 커서 방식)
     * ========================================================= */
    @Bean
    public MyBatisCursorItemReader<Order> orderItemReader() {
        log.info("orderItemReader");
        return new MyBatisCursorItemReaderBuilder<Order>()
                .sqlSessionFactory(sqlSessionFactory)
                // mapper 메서드 풀 경로
                .queryId("com.example.orderbatch.mapper.OrderMapper.selectOrdersByStatus")
                // 파라미터 전달
                .parameterValues(Map.of("status", Order.OrderStatus.PENDING.name()))
                .build();
    }

    /* =========================================================
     *  Step
     * ========================================================= */
    @Bean
    public Step orderProcessingStep(JobRepository jobRepository,
                                    PlatformTransactionManager transactionManager) {
        return new StepBuilder("orderProcessingStep", jobRepository)
                .<Order, Order>chunk(chunkSize, transactionManager)
                .reader(orderItemReader())
                .processor(orderItemProcessor)
                .processor(productOrderItemProcessor)
                .writer(orderItemWriter)
                // Skip 정책: IllegalArgumentException 발생 시 최대 5건까지 스킵
                .faultTolerant()
                    .skipLimit(5)
                    .skip(IllegalArgumentException.class)
                    .listener(orderSkipListener)
                .build();
    }

    /* =========================================================
     *  Job
     * ========================================================= */
    @Bean
    public Job orderProcessingJob(JobRepository jobRepository,
                                  Step orderProcessingStep) {
        return new JobBuilder("orderProcessingJob", jobRepository)
                .incrementer(new RunIdIncrementer())     // 매 실행마다 새 Job Instance
                .listener(orderJobListener)
                .start(orderProcessingStep)
                .build();
    }
}
