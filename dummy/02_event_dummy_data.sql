-- 이벤트 & 당첨 정책 더미 데이터

START TRANSACTION;

-- 1. 이벤트 생성
-- 2025-11-07 시작, 30일간 진행 → 2025-12-06 23:59:59 종료
INSERT INTO event (
    id,
    name,
    description,
    start_dt,
    end_dt,
    max_daily_try,
    status,
    created_dt,
    updated_dt,
    participation_start_time,
    participation_end_time
) VALUES (
    1,
    '2025년 11월 출석 이벤트',
    '2025-11-07부터 30일간 진행되는 출석/참여 이벤트입니다.',
    '2025-11-06 15:00:00',
    '2025-12-06 14:59:59',
    1,
    'OPEN',              -- 필요하면 OPEN 으로 변경해서 사용
    NOW(),
    NOW(),
    '06:00:00',           -- 참여 가능 시작 시간
    '23:00:00'            -- 참여 가능 종료 시간
);

-- 2. 당첨 정책 생성
-- 매일 1 / 5 / 10 / 20 / 50 번째 참여자
-- NTH_ORDER + PER_DAY 기준
INSERT INTO reward_policy (
    event_id,
    name,
    start_dt,
    end_dt,
    target_order,
    nth_scope,
    user_limit_total,
    user_limit_per_day,
    reward_type,
    reward_value,
    created_dt,
    updated_dt
) VALUES
    (
        1,
        '매일 1번째 참여자',
        '2025-11-06 15:00:00',
        '2025-12-06 14:59:59',
        1,
        'PER_DAY',
        1,
        1,
        'POINT',
        'NTH_ORDER_1',      -- 실제 운영 시 포인트값/쿠폰코드 등으로 교체
        NOW(),
        NOW()
    ),
    (
        1,
        '매일 5번째 참여자',
        '2025-11-06 15:00:00',
        '2025-12-06 14:59:59',
        5,
        'PER_DAY',
        1,
        1,
        'POINT',
        'NTH_ORDER_5',
        NOW(),
        NOW()
    ),
    (
        1,
        '매일 10번째 참여자',
        '2025-11-06 15:00:00',
        '2025-12-06 14:59:59',
        10,
        'PER_DAY',
        1,
        1,
        'POINT',
        'NTH_ORDER_10',
        NOW(),
        NOW()
    ),
    (
        1,
        '매일 20번째 참여자',
        '2025-11-06 15:00:00',
        '2025-12-06 14:59:59',
        20,
        'PER_DAY',
        1,
        1,
        'POINT',
        'NTH_ORDER_20',
        NOW(),
        NOW()
    ),
    (
        1,
        '매일 50번째 참여자',
        '2025-11-06 15:00:00',
        '2025-12-06 14:59:59',
        50,
        'PER_DAY',
        1,
        1,
        'POINT',
        'NTH_ORDER_50',
        NOW(),
        NOW()
    );

COMMIT;
