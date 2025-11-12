ALTER TABLE `user`
    ADD COLUMN branch_code VARCHAR(50) NULL AFTER employee_number,
    DROP INDEX `UK_user_phone_number`,
    ADD UNIQUE KEY `uk_user_name_employee_number` (`name`, `employee_number`);
