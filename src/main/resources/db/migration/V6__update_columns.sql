ALTER TABLE quiz
    CHANGE COLUMN is_active active TINYINT(1) NOT NULL DEFAULT 1;

ALTER TABLE quiz_option
    CHANGE COLUMN is_correct correct TINYINT(1) NOT NULL DEFAULT 0;

ALTER TABLE quiz_participation
    CHANGE COLUMN is_correct correct TINYINT(1) NOT NULL;

ALTER TABLE quiz_participation_answer
    CHANGE COLUMN is_correct correct TINYINT(1) NOT NULL;
