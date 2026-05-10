-- 샘플 주문 데이터 (PENDING 상태)
--INSERT INTO orders (customer_id, customer_name, product_code, product_name, quantity, unit_price, total_amount, order_status, ordered_at)
--VALUES
--    (1001, '김철수', 'PROD-001', '무선 블루투스 이어폰',  2, 45000.00,  90000.00, 'PENDING', DATEADD('DAY', -1, CURRENT_TIMESTAMP)),
--    (1002, '이영희', 'PROD-002', '스마트워치',            1, 250000.00, 250000.00, 'CANCELLED', DATEADD('DAY', -1, CURRENT_TIMESTAMP)),
--    (1003, '박민준', 'PROD-003', '노트북 파우치',          3, 18000.00,  54000.00, 'PENDING', DATEADD('DAY', -1, CURRENT_TIMESTAMP)),
--    (1004, '최지수', 'PROD-004', 'USB-C 허브',            2, 35000.00,  70000.00, 'PENDING', DATEADD('DAY', -1, CURRENT_TIMESTAMP)),
--    (1005, '정민호', 'PROD-001', '무선 블루투스 이어폰',  1, 45000.00,  45000.00, 'PENDING', DATEADD('DAY', -1, CURRENT_TIMESTAMP)),
--    (1006, '한소희', 'PROD-005', '기계식 키보드',          1, 120000.00, 120000.00, 'PENDING', DATEADD('DAY', -1, CURRENT_TIMESTAMP)),
--    (1007, '윤서준', 'PROD-006', '27인치 모니터',          1, 350000.00, 350000.00, 'PENDING', DATEADD('DAY', -1, CURRENT_TIMESTAMP)),
--    (1008, '강다은', 'PROD-007', '마우스 패드 (대형)',      2, 15000.00,  30000.00, 'PENDING', DATEADD('DAY', -1, CURRENT_TIMESTAMP)),
--    (1009, '임재현', 'PROD-002', '스마트워치',             2, 250000.00, 500000.00, 'PENDING', DATEADD('DAY', -1, CURRENT_TIMESTAMP)),
--    (1010, '송유진', 'PROD-008', '웹캠 HD',               1, 75000.00,  75000.00, 'PENDING', DATEADD('DAY', -1, CURRENT_TIMESTAMP)),
--    -- 이미 처리된 주문 (PROCESSED 상태 - 배치 대상 아님)
--    (1011, '오성민', 'PROD-003', '노트북 파우치',           1, 18000.00,  18000.00, 'PROCESSED', DATEADD('DAY', -2, CURRENT_TIMESTAMP)),
--    (1012, '배수진', 'PROD-005', '기계식 키보드',           1, 120000.00, 120000.00, 'PROCESSED', DATEADD('DAY', -2, CURRENT_TIMESTAMP)),
--    -- 취소된 주문
--    (1013, '신동욱', 'PROD-009', '무선 충전기',             1, 28000.00,  28000.00, 'CANCELLED', DATEADD('DAY', -1, CURRENT_TIMESTAMP));

-- mysql에서는 DATE_SUB 함수를 사용하여 날짜를 계산합니다. 아래는 MySQL용으로 수정된 샘플 주문 데이터 삽입 SQL입니다.
INSERT INTO orders (customer_id, customer_name, product_code, product_name, quantity, unit_price, total_amount, order_status, ordered_at)
VALUES
    (1001, '김철수', 'PROD-001', '무선 블루투스 이어폰',  2, 45000.00,  90000.00, 'PENDING', DATE_SUB(NOW(), INTERVAL 1 DAY)),
    (1002, '이영희', 'PROD-002', '스마트워치',            1, 250000.00, 250000.00, 'CANCELLED', DATE_SUB(NOW(), INTERVAL 1 DAY)),
    (1003, '박민준', 'PROD-003', '노트북 파우치',          3, 18000.00,  54000.00, 'PENDING', DATE_SUB(NOW(), INTERVAL 1 DAY)),
    (1004, '최지수', 'PROD-004', 'USB-C 허브',            2, 35000.00,  70000.00, 'PENDING', DATE_SUB(NOW(), INTERVAL 1 DAY)),
    (1005, '정민호', 'PROD-001', '무선 블루투스 이어폰',  1, 45000.00,  45000.00, 'PENDING', DATE_SUB(NOW(), INTERVAL 1 DAY)),
    (1006, '한소희', 'PROD-005', '기계식 키보드',          1, 120000.00, 120000.00, 'PENDING', DATE_SUB(NOW(), INTERVAL 1 DAY)),
    (1007, '윤서준', 'PROD-006', '27인치 모니터',          1, 350000.00, 350000.00, 'PENDING', DATE_SUB(NOW(), INTERVAL 1 DAY)),
    (1008, '강다은', 'PROD-007', '마우스 패드 (대형)',      2, 15000.00,  30000.00, 'PENDING', DATE_SUB(NOW(), INTERVAL 1 DAY)),
    (1009, '임재현', 'PROD-002', '스마트워치',             2, 250000.00, 500000.00, 'PENDING', DATE_SUB(NOW(), INTERVAL 1 DAY)),
    (1010, '송유진', 'PROD-008', '웹캠 HD',               1, 75000.00,  75000.00, 'PENDING', DATE_SUB(NOW(), INTERVAL 1 DAY)),
    -- 이미 처리된 주문 (PROCESSED 상태 - 배치 대상 아님)
    (1011, '오성민', 'PROD-003', '노트북 파우치',           1, 18000.00,  18000.00, 'PROCESSED', DATE_SUB(NOW(), INTERVAL 2 DAY)),
    (1012, '배수진', 'PROD-005', '기계식 키보드',           1, 120000.00, 120000.00, 'PROCESSED', DATE_SUB(NOW(), INTERVAL 2 DAY)),
    -- 취소된 주문
    (1013, '신동욱', 'PROD-009', '무선 충전기',             1, 28000.00,  28000.00, 'CANCELLED', DATE_SUB(NOW(), INTERVAL 1 DAY));

