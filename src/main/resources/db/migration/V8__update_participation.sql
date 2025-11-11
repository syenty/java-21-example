ALTER TABLE quiz_participation
    DROP COLUMN score;

ALTER TABLE quiz_participation
    RENAME TO event_participation;

ALTER TABLE quiz_participation_answer
    RENAME TO event_participation_answer;

ALTER TABLE event_participation_answer
    DROP INDEX uk_qpa_participation_quiz,
    ADD CONSTRAINT uk_epa_participation_quiz UNIQUE (participation_id, quiz_id);
