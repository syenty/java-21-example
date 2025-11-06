CREATE TABLE event (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    name             VARCHAR(200) NOT NULL,
    description      TEXT,
    start_dt         DATETIME     NOT NULL,
    end_dt           DATETIME     NOT NULL,
    max_daily_try    INT          NOT NULL DEFAULT 1,
    status           ENUM('READY','OPEN','CLOSED') NOT NULL DEFAULT 'READY',
    created_dt       DATETIME     NOT NULL,
    updated_dt       DATETIME     NOT NULL,
    INDEX idx_event_period_status (status, start_dt, end_dt)
);

CREATE TABLE quiz (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    event_id        BIGINT NOT NULL,
    type            ENUM('OX','MULTIPLE_CHOICE','SHORT_ANSWER') NOT NULL,
    question_text   TEXT   NOT NULL,
    correct_text    VARCHAR(500) NULL,
    question_order  INT NOT NULL DEFAULT 1,
    is_active       TINYINT(1) NOT NULL DEFAULT 1,
    created_dt      DATETIME NOT NULL,
    CONSTRAINT fk_quiz_event
        FOREIGN KEY (event_id) REFERENCES event(id),
    INDEX idx_quiz_event_order (event_id, question_order)
);

CREATE TABLE quiz_option (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    quiz_id          BIGINT NOT NULL,
    option_key       VARCHAR(10) NOT NULL,    -- 'O','X','A','B','C' 등
    option_text      VARCHAR(500) NOT NULL,
    is_correct       TINYINT(1) NOT NULL DEFAULT 0,
    option_order     INT NOT NULL DEFAULT 1,
    CONSTRAINT fk_quiz_option_quiz
        FOREIGN KEY (quiz_id) REFERENCES quiz(id),
    INDEX idx_quiz_option_quiz (quiz_id)
);

CREATE TABLE event_daily_sequence (
    event_id   BIGINT NOT NULL,
    seq_date   DATE   NOT NULL,    -- 서울 기준 날짜 (KST)
    last_seq   INT    NOT NULL,
    PRIMARY KEY (event_id, seq_date),
    CONSTRAINT fk_daily_seq_event
        FOREIGN KEY (event_id) REFERENCES event(id)
);

CREATE TABLE quiz_participation (
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    event_id           BIGINT NOT NULL,
    user_id            BIGINT NOT NULL,
    participation_dt    DATETIME NOT NULL, -- UTC
    participation_date  DATE     NOT NULL, -- KST 기준 날짜
    daily_order        INT      NOT NULL, -- event + date 기준 N번째 참여
    is_correct         TINYINT(1) NOT NULL,  -- 전체 문항 기준 성공 여부(옵션)
    score              INT      NOT NULL DEFAULT 0,
    correct_count      INT      NOT NULL DEFAULT 0,
    total_questions    INT      NOT NULL DEFAULT 0,
    CONSTRAINT fk_participation_event
        FOREIGN KEY (event_id) REFERENCES event(id),
    CONSTRAINT fk_participation_user
        FOREIGN KEY (user_id) REFERENCES user(id),
    -- 순번/당첨자 조회용
    INDEX idx_participation_event_date_order (event_id, participation_date, daily_order),
    INDEX idx_participation_user_date (user_id, participation_date)
);

CREATE TABLE quiz_participation_answer (
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    participation_id   BIGINT NOT NULL,
    quiz_id            BIGINT NOT NULL,
    option_id          BIGINT NULL,         -- 객관식/OX일 때 선택한 보기
    answer_text        VARCHAR(500) NULL,   -- 주관식 답
    is_correct         TINYINT(1) NOT NULL,
    answer_dt        DATETIME NOT NULL,
    CONSTRAINT fk_qpa_participation
        FOREIGN KEY (participation_id) REFERENCES quiz_participation(id),
    CONSTRAINT fk_qpa_quiz
        FOREIGN KEY (quiz_id) REFERENCES quiz(id),
    CONSTRAINT fk_qpa_option
        FOREIGN KEY (option_id) REFERENCES quiz_option(id),
    INDEX idx_qpa_participation (participation_id),
    INDEX idx_qpa_quiz (quiz_id)
);

CREATE TABLE reward_policy (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    event_id            BIGINT NOT NULL,
    name                VARCHAR(200) NOT NULL, -- 정책 이름 (관리 화면용)

    -- 정책 유형: 선착순 / 특정 순번
    policy_type         ENUM('FIRST_COME', 'NTH_ORDER') NOT NULL,

    -- 정책 적용 기간 (UTC)
    start_dt            DATETIME NOT NULL,
    end_dt              DATETIME NOT NULL,

    ------------------------------------------------------------------
    -- [선착순(FIRST_COME) 관련 필드]
    --  - 선착순 N명: winner_limit_total = 100 (이벤트 전체 기준)
    --  - 매일 선착순 N명: winner_limit_per_day = 100
    ------------------------------------------------------------------
    winner_limit_total   INT NULL,  -- 정책 전체 기간 동안 최대 당첨 인원 수
    winner_limit_per_day INT NULL,  -- 날짜(KST) 기준 최대 당첨 인원 수

    ------------------------------------------------------------------
    -- [특정 순번(NTH_ORDER) 관련 필드]
    --  - target_order = 5  => 5번째 참여자
    --  - nth_scope = 'PER_DAY'   => 매일 5번째
    --  - nth_scope = 'EVENT'     => 이벤트 전체 기간 동안 한 번만 5번째
    ------------------------------------------------------------------
    target_order         INT NULL,  -- 5, 10 같은 자연수 (N번째)
    nth_scope            ENUM('PER_DAY','EVENT') NULL,

    ------------------------------------------------------------------
    -- [유저별 중복 당첨 제한] (UNIQUE로 막지 않고, 로직에서 COUNT로 체크)
    ------------------------------------------------------------------
    user_limit_total     INT NULL,  -- 이 정책으로 한 유저가 최대 몇 번까지 당첨 가능
    user_limit_per_day   INT NULL,  -- 하루 기준 최대 당첨 횟수

    ------------------------------------------------------------------
    -- [지급 내용]
    ------------------------------------------------------------------
    reward_type          ENUM('POINT','COUPON','GOODS') NOT NULL,
    reward_value         VARCHAR(200) NOT NULL, -- 포인트값/쿠폰코드/상품코드 등

    created_dt           DATETIME NOT NULL,
    updated_dt           DATETIME NOT NULL,

    CONSTRAINT fk_reward_policy_event
        FOREIGN KEY (event_id) REFERENCES event(id),

    -- 조회용 인덱스
    INDEX idx_reward_policy_event (event_id),
    INDEX idx_reward_policy_period (event_id, start_dt, end_dt),
    INDEX idx_reward_policy_type (event_id, policy_type)
);

CREATE TABLE reward_issue (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    event_id         BIGINT NOT NULL,
    user_id          BIGINT NOT NULL,
    participation_id BIGINT NOT NULL,
    reward_policy_id BIGINT NOT NULL,
    reward_date      DATE   NOT NULL, -- KST 기준 날짜
    issued_dt        DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_reward_issue_event
        FOREIGN KEY (event_id) REFERENCES event(id),
    CONSTRAINT fk_reward_issue_user
        FOREIGN KEY (user_id) REFERENCES user(id),
    CONSTRAINT fk_reward_issue_participation
        FOREIGN KEY (participation_id) REFERENCES quiz_participation(id),
    CONSTRAINT fk_reward_issue_policy
        FOREIGN KEY (reward_policy_id) REFERENCES reward_policy(id),

    -- 정책별 전체/일별 당첨자 수 조회용
    INDEX idx_issue_policy (reward_policy_id),
    INDEX idx_issue_policy_date (reward_policy_id, reward_date),

    -- 유저별 중복 당첨 제한 체크용
    INDEX idx_issue_policy_user (reward_policy_id, user_id),
    INDEX idx_issue_policy_user_date (reward_policy_id, user_id, reward_date),

    INDEX idx_issue_event (event_id)
);