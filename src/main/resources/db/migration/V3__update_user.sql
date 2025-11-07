ALTER TABLE user
  CHANGE COLUMN organization_code employee_number VARCHAR(255) NOT NULL;

ALTER TABLE user
  ADD external_id CHAR(36) NULL;

UPDATE user
  SET external_id = UUID()
  WHERE external_id IS NULL OR external_id = '';

ALTER TABLE user
  MODIFY COLUMN external_id CHAR(36) NOT NULL;

ALTER TABLE user
  ADD CONSTRAINT uq_user_external_id UNIQUE (external_id);

ALTER TABLE user
  ADD phone_verified TINYINT(1) NOT NULL DEFAULT 0,
  ADD phone_verified_dt DATETIME NULL;

ALTER TABLE user
  ADD CONSTRAINT uq_user_employee_number_name UNIQUE (employee_number, name);
