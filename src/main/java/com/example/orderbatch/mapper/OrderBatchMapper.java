package com.example.orderbatch.mapper;

import com.example.orderbatch.domain.OrderBatchSummary;
import com.example.orderbatch.domain.OrderErrorLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 배치 집계 및 에러 로그 Mapper
 */
@Mapper
public interface OrderBatchMapper {

    /**
     * 배치 집계 결과 저장
     */
    int insertBatchSummary(OrderBatchSummary summary);

    /**
     * 에러 로그 저장
     */
    int insertErrorLog(OrderErrorLog errorLog);
}
