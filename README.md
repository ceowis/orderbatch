# Order Batch — Spring Boot + Spring Batch + MyBatis 샘플

PENDING 상태의 주문을 읽어 유효성 검사 후 PROCESSED로 변경하는 배치 프로젝트입니다.

---

## 프로젝트 구조

```
order-batch/
├── pom.xml
└── src/main/
    ├── java/com/example/orderbatch/
    │   ├── OrderBatchApplication.java          # 메인 + CommandLineRunner (데모)
    │   ├── config/
    │   │   └── BatchConfig.java                # Job / Step / Reader Bean 설정
    │   ├── domain/
    │   │   ├── Order.java                      # 주문 도메인 + OrderStatus enum
    │   │   ├── OrderBatchSummary.java           # 집계 결과
    │   │   └── OrderErrorLog.java              # 에러 로그
    │   ├── mapper/
    │   │   ├── OrderMapper.java                # 주문 CRUD
    │   │   └── OrderBatchMapper.java           # 집계/에러 저장
    │   ├── batch/
    │   │   ├── processor/OrderItemProcessor.java  # 유효성 검사 + 금액 재계산
    │   │   ├── writer/OrderItemWriter.java        # DB 상태 업데이트
    │   │   ├── step/OrderSkipListener.java        # Skip 이벤트 처리
    │   │   └── job/OrderJobListener.java          # Job 시작/종료 로깅 + 집계 저장
    │   └── service/
    │       └── OrderBatchService.java          # JobLauncher 래퍼
    └── resources/
        ├── application.yml
        ├── schema.sql                          # 테이블 DDL
        ├── data.sql                            # 샘플 데이터
        └── mapper/
            ├── OrderMapper.xml
            └── OrderBatchMapper.xml
```

---

## 배치 흐름

```
[orderProcessingJob]
        │
        └─▶ [orderProcessingStep]
                │
                ├─ Reader   : MyBatisCursorItemReader
                │             → selectOrdersByStatus(PENDING)
                │             → 커서 방식으로 대용량 처리
                │
                ├─ Processor: OrderItemProcessor
                │             → null 반환 시 skip (filter)
                │             → 금액 재계산
                │             → IllegalArgumentException → skip (최대 5건)
                │
                └─ Writer   : OrderItemWriter
                              → updateOrderProcessed (PENDING → PROCESSED)
```

---

## 주요 기술 포인트

| 항목 | 내용 |
|------|------|
| **Reader** | `MyBatisCursorItemReader` — 커서 스트리밍으로 OOM 없이 대용량 처리 |
| **Chunk** | `chunk-size: 10` — 10건 단위 트랜잭션 |
| **Skip** | `faultTolerant().skipLimit(5)` — IllegalArgumentException 5건까지 허용 |
| **SkipListener** | skip된 주문을 `FAILED`로 변경 + `order_error_log` 저장 |
| **JobListener** | Job 시작/종료 로그 + `order_batch_summary` 집계 저장 |
| **멱등성** | `updateOrderProcessed`는 `WHERE order_status = 'PENDING'` 조건으로 중복 처리 방지 |

---

## 실행 방법

```bash
# 빌드 및 실행 (H2 인메모리 DB 사용)
./mvnw spring-boot:run

# 테스트
./mvnw test
```

---

## MySQL 전환 시

`application.yml`에서 datasource 변경:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/orderdb?useSSL=false&serverTimezone=Asia/Seoul
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: root
    password: password
```

`pom.xml`에서 mysql-connector-j 의존성 주석 해제.
