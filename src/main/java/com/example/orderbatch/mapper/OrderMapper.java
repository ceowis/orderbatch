package com.example.orderbatch.mapper;

import com.example.orderbatch.domain.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 주문 MyBatis Mapper
 */
@Mapper
public interface OrderMapper {

    /**
     * 특정 상태의 주문 목록 조회 (페이징 포함 - MyBatisCursorItemReader용)
     */
    List<Order> selectOrdersByStatus(@Param("status") String status);

    /**
     * 주문 상태 업데이트 (단건)
     */
    int updateOrderStatus(@Param("orderId") Long orderId,
                          @Param("status") String status);

    /**
     * 주문 처리 완료 업데이트 (processedAt 포함)
     */
    int updateOrderProcessed(@Param("orderId") Long orderId);

    /**
     * 주문 상태를 FAILED로 변경
     */
    int updateOrderFailed(@Param("orderId") Long orderId);

    /**
     * 상태별 주문 수 조회
     */
    Map<String, Object> selectOrderCountByStatus();
}
