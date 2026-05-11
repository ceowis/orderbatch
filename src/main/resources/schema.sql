-- 주문 테이블
CREATE TABLE IF NOT EXISTS orders (
    order_id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id     BIGINT          NOT NULL,
    customer_name   VARCHAR(100)    NOT NULL,
    product_code    VARCHAR(50)     NOT NULL,
    product_name    VARCHAR(200)    NOT NULL,
    quantity        INT             NOT NULL,
    unit_price      DECIMAL(12, 2)  NOT NULL,
    total_amount    DECIMAL(14, 2)  NOT NULL,
    order_status    VARCHAR(20)     NOT NULL DEFAULT 'PENDING',
    ordered_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    processed_at    TIMESTAMP,
    created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 처리 결과 집계 테이블
CREATE TABLE IF NOT EXISTS order_batch_summary (
    summary_id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    job_execution_id    BIGINT,
    batch_date          DATE            NOT NULL,
    total_count         INT             NOT NULL DEFAULT 0,
    success_count       INT             NOT NULL DEFAULT 0,
    fail_count          INT             NOT NULL DEFAULT 0,
    total_amount        DECIMAL(18, 2)  NOT NULL DEFAULT 0,
    created_at          TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 주문 처리 실패 로그
CREATE TABLE IF NOT EXISTS order_error_log (
    error_id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id        BIGINT,
    error_message   VARCHAR(1000),
    step_name       VARCHAR(100),
    occurred_at     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- product_sales_summary 테이블은 "어떤 상품이, 언제, 얼마나 팔렸는가"
CREATE TABLE IF NOT EXISTS product_sales_summary (
    product_summary_id  BIGINT AUTO_INCREMENT PRIMARY KEY,
    batch_date          DATE            NOT NULL, -- 집계 대상 날짜
    product_code        VARCHAR(50)     NOT NULL, -- 상품 코드
    product_name        VARCHAR(200)    NOT NULL, -- 상품명 (가독성 위해 포함)
    total_quantity      INT             NOT NULL DEFAULT 0, -- 총 판매 수량
    total_revenue       DECIMAL(18, 2)  NOT NULL DEFAULT 0, -- 총 매출 금액
    order_count         INT             NOT NULL DEFAULT 0, -- 총 주문 건수
    created_at          TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- 같은 날짜에 동일 상품이 중복 집계되지 않도록 유니크 인덱스 추가
    UNIQUE KEY (batch_date, product_code)
);

-- 인덱스
--CREATE INDEX IF NOT EXISTS idx_orders_status ON orders(order_status);
--CREATE INDEX IF NOT EXISTS idx_orders_ordered_at ON orders(ordered_at);
