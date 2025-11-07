ALTER TABLE quiz
  ADD quiz_date DATE NOT NULL;

CREATE INDEX idx_quiz_event_date_order
  ON quiz (event_id, quiz_date, question_order);
