package com.example.orderbatch.batch.writer;

import com.example.orderbatch.domain.Order;
import com.example.orderbatch.domain.ProductSalesSummary;
import com.example.orderbatch.mapper.OrderBatchMapper;
import com.example.orderbatch.mapper.OrderMapper;
import com.example.orderbatch.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 주문 처리 Writer
 * - 처리된 주문을 PROCESSED 상태로 업데이트
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OrderItemWriter implements ItemWriter<Order> {

    private final OrderMapper orderMapper;
    private final ProductMapper productMapper;   // 내가 새로 만든 매퍼 (추가)

//    @Override
//    public void write(Chunk<? extends Order> chunk) throws Exception {
//        log.info("chunk.size() : {} ", chunk.size()); // 1건이 필터되었으니까 9건
//        log.info("Writing chunk of {} orders", chunk.toString());
//
//        log.info("################## 주문 완료처리 시작 ##############");
//
//        for (Order order : chunk) {
//            int updated = orderMapper.updateOrderProcessed(order.getOrderId());
//            if (updated > 0) {
//                log.debug("Order processed: orderId={}, customer={}, amount={}",
//                        order.getOrderId(), order.getCustomerName(), order.getTotalAmount());
//            } else {
//                log.warn("Order update skipped (possibly already processed): orderId={}",
//                        order.getOrderId());
//            }
//        }
//
//        log.info("Chunk write completed: {} items", chunk.size());
//    }


    @Override
    public void write(Chunk<? extends Order> chunkItems) throws Exception {
        log.info("target data {} , ", chunkItems);

        for(Order order : chunkItems) {
            log.info("target {} : ", order);

            int updated = orderMapper.updateOrderProcessed(order.getOrderId()); // 주문 처리 완료 업데이트
            if(updated > 0){
                log.debug("처리한 데이터 : orderId={}, customer={}, aumount={} ",  order.getOrderId(), order.getCustomerName(), order.getTotalAmount());
            }else{
                log.debug("처리하지 못한 데이터 : orderId={}, customer={}, aumount={} ",  order.getOrderId(), order.getCustomerName(), order.getTotalAmount());
            }
        }

        // 로직 추가
        // 상품별 통계로직 추가
        Map<String, ProductSalesSummary> map = new HashMap<>();

        for(Order order : chunkItems) {
            String code = order.getProductCode();
            if(map.containsKey(code)) {
                // 이미 존재하는 상품이면 기존 집계 정보 업데이트
                ProductSalesSummary existSummary = map.get(code);
                existSummary.setTotalQuantity(existSummary.getTotalQuantity() + order.getQuantity()); // 수량 누적
                // 금액 합산 (BigDecimal 사용법)
                existSummary.setTotalRevenue(existSummary.getTotalRevenue() + order.getTotalAmount().doubleValue());
                existSummary.setOrderCount(existSummary.getOrderCount() + 1);
            }else{
                // 새로운 상품이면 새 집계 정보 생성
                ProductSalesSummary newSummary = ProductSalesSummary.builder()
                        .productCode(code)
                        .productName(order.getProductName())
                        .totalQuantity(order.getQuantity())
                        .totalRevenue(order.getTotalAmount().doubleValue())
                        .orderCount(1)
                        .batchDate(new java.util.Date()) // 실행 시점 날짜
                        .build();
                map.put(code, newSummary);
            }
        }
        // 3. DB 저장
        for(ProductSalesSummary summary : map.values()) {
            log.info("상품별 통계 : {} ", summary);
            productMapper.upsertProductSalesSummary(summary); // 상품별 통계 DB 저장
        }

        log.info("Chunk write completed:  {} ", chunkItems.size());

    }
}
