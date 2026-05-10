package com.example.orderbatch.batch.writer;

import com.example.orderbatch.domain.Order;
import com.example.orderbatch.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

/**
 * 주문 처리 Writer
 * - 처리된 주문을 PROCESSED 상태로 업데이트
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OrderItemWriter implements ItemWriter<Order> {

    private final OrderMapper orderMapper;

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
    public void write(Chunk<? extends Order> chunk) throws Exception {
        log.info("target data {} , ", chunk);

        for(Order order : chunk) {
            log.info("target {} : ", order);

            int updated = orderMapper.updateOrderProcessed(order.getOrderId());
            if(updated > 0){
                log.debug("처리한 데이터 : orderId={}, customer={}, aumount={} ",  order.getOrderId(), order.getCustomerName(), order.getTotalAmount());
            }else{
                log.debug("처리하지 못한 데이터 : orderId={}, customer={}, aumount={} ",  order.getOrderId(), order.getCustomerName(), order.getTotalAmount());
            }
        }
        log.info("Chunk write completed:  {} ", chunk.size());
    }
}
