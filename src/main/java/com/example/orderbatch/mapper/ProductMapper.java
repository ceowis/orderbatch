package com.example.orderbatch.mapper;

import com.example.orderbatch.domain.ProductSalesSummary;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper {
    /**
        * 상품별 판매 집계 저장
        */
        int upsertProductSalesSummary(ProductSalesSummary summary);

}
