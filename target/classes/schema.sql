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

-- 인덱스
CREATE INDEX IF NOT EXISTS idx_orders_status ON orders(order_status);
CREATE INDEX IF NOT EXISTS idx_orders_ordered_at ON orders(ordered_at);
