CREATE TABLE event_participation (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    event_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    participation_dt DATETIME NOT NULL,
    participation_date DATE NOT NULL,
    daily_order INT NOT NULL,
    correct TINYINT(1) NOT NULL,
    correct_count INT NOT NULL DEFAULT 0,
    total_questions INT NOT NULL DEFAULT 0,
    CONSTRAINT fk_participation_event FOREIGN KEY (event_id) REFERENCES event (id),
    CONSTRAINT fk_participation_user FOREIGN KEY (user_id) REFERENCES user (id),
    INDEX idx_participation_event_date_order (event_id, participation_date, daily_order),
    INDEX idx_participation_user_date (user_id, participation_date)
);

CREATE TABLE event_participation_answer (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    participation_id BIGINT NOT NULL,
    quiz_id BIGINT NOT NULL,
    option_id BIGINT NULL,
    answer_text VARCHAR(500) NULL,
    correct TINYINT(1) NOT NULL,
    answer_dt DATETIME NOT NULL,
    CONSTRAINT fk_epa_participation FOREIGN KEY (participation_id) REFERENCES event_participation (id),
    CONSTRAINT fk_epa_quiz FOREIGN KEY (quiz_id) REFERENCES quiz (id),
    CONSTRAINT fk_epa_option FOREIGN KEY (option_id) REFERENCES quiz_option (id),
    CONSTRAINT uk_epa_participation_quiz UNIQUE (participation_id, quiz_id),
    INDEX idx_epa_participation (participation_id),
    INDEX idx_epa_quiz (quiz_id)
);
