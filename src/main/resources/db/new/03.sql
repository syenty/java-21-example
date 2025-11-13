CREATE TABLE quiz (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    event_id BIGINT NOT NULL,
    type ENUM('OX', 'MULTIPLE_CHOICE', 'SHORT_ANSWER') NOT NULL,
    question_text TEXT NOT NULL,
    correct_text VARCHAR(500) NULL,
    question_order INT NOT NULL DEFAULT 1,
    active TINYINT(1) NOT NULL DEFAULT 1,
    created_dt DATETIME NOT NULL,
    quiz_date DATE NOT NULL,
    CONSTRAINT fk_quiz_event FOREIGN KEY (event_id) REFERENCES event (id),
    INDEX idx_quiz_event_order (event_id, question_order),
    INDEX idx_quiz_event_date_order (event_id, quiz_date, question_order)
);

CREATE TABLE quiz_option (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    quiz_id BIGINT NOT NULL,
    option_key VARCHAR(10) NOT NULL,
    option_text VARCHAR(500) NOT NULL,
    correct TINYINT(1) NOT NULL DEFAULT 0,
    option_order INT NOT NULL DEFAULT 1,
    CONSTRAINT fk_quiz_option_quiz FOREIGN KEY (quiz_id) REFERENCES quiz (id),
    INDEX idx_quiz_option_quiz (quiz_id)
);
