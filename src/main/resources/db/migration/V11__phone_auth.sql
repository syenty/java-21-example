CREATE TABLE phone_auth_request (
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    request_id        CHAR(36) NOT NULL UNIQUE,
    phone_number      VARCHAR(20) NOT NULL,
    verification_code CHAR(4) NOT NULL,
    name              VARCHAR(255) NOT NULL,
    employee_number   VARCHAR(255) NOT NULL,
    created_dt        DATETIME NOT NULL,
    expired_dt        DATETIME NOT NULL,
    verified          BOOLEAN DEFAULT false NOT NULL,
    verified_dt       DATETIME NULL,
    INDEX idx_phone_created (phone_number, created_dt DESC)
);
