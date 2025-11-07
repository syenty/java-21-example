ALTER TABLE event
  ADD participation_start_time TIME NULL,
  ADD participation_end_time TIME NULL;

CREATE TABLE event_attendance (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  event_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  attendance_dt DATETIME NOT NULL,
  attendance_date DATE NOT NULL,
  CONSTRAINT fk_event_attendance_event FOREIGN KEY (event_id) REFERENCES event(id),
  CONSTRAINT fk_event_attendance_user FOREIGN KEY (user_id) REFERENCES user(id),
  CONSTRAINT uq_event_attendance UNIQUE (event_id, user_id, attendance_date)
);
