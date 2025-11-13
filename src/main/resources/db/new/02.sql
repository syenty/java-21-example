CREATE TABLE event (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    description TEXT NULL,
    start_dt DATETIME NOT NULL,
    end_dt DATETIME NOT NULL,
    max_daily_try INT NOT NULL DEFAULT 1,
    status ENUM('READY', 'OPEN', 'CLOSED') NOT NULL DEFAULT 'READY',
    created_dt DATETIME NOT NULL,
    updated_dt DATETIME NOT NULL,
    participation_start_time TIME NULL,
    participation_end_time TIME NULL,
    reward_limit_per_user INT NULL,
    INDEX idx_event_period_status (status, start_dt, end_dt)
);

CREATE TABLE event_attendance (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    event_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    attendance_dt DATETIME NOT NULL,
    attendance_date DATE NOT NULL,
    CONSTRAINT fk_event_attendance_event FOREIGN KEY (event_id) REFERENCES event (id),
    CONSTRAINT fk_event_attendance_user FOREIGN KEY (user_id) REFERENCES user (id),
    CONSTRAINT uq_event_attendance UNIQUE (event_id, user_id, attendance_date)
);

CREATE TABLE event_daily_sequence (
    event_id BIGINT NOT NULL,
    seq_date DATE NOT NULL,
    last_seq INT NOT NULL,
    PRIMARY KEY (event_id, seq_date),
    CONSTRAINT fk_daily_seq_event FOREIGN KEY (event_id) REFERENCES event (id)
);
