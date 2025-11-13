CREATE TABLE user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_dt DATETIME NOT NULL,
    updated_dt DATETIME NOT NULL,
    name VARCHAR(255) NOT NULL,
    phone_number VARCHAR(255) NOT NULL,
    employee_number VARCHAR(255) NOT NULL,
    branch_code VARCHAR(50) NULL,
    external_id CHAR(36) NOT NULL,
    phone_verified TINYINT(1) NOT NULL DEFAULT 0,
    phone_verified_dt DATETIME NULL,
    blocked BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT uq_user_external_id UNIQUE (external_id),
    CONSTRAINT uk_user_name_employee_number UNIQUE (name, employee_number)
);

CREATE TABLE admin (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_dt DATETIME NOT NULL,
    updated_dt DATETIME NOT NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,
    CONSTRAINT uq_admin_email UNIQUE (email)
);

CREATE TABLE phone_auth_request (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    request_id CHAR(36) NOT NULL UNIQUE,
    phone_number VARCHAR(20) NOT NULL,
    verification_code CHAR(4) NOT NULL,
    name VARCHAR(255) NOT NULL,
    employee_number VARCHAR(255) NOT NULL,
    created_dt DATETIME NOT NULL,
    expired_dt DATETIME NOT NULL,
    verified BOOLEAN NOT NULL DEFAULT FALSE,
    verified_dt DATETIME NULL,
    INDEX idx_phone_auth_request_created (phone_number, created_dt DESC)
);
