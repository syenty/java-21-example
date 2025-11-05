-- 사용자 테이블
CREATE TABLE user
(
    id                BIGINT AUTO_INCREMENT NOT NULL,
    created_dt        datetime              NOT NULL,
    updated_dt        datetime              NOT NULL,
    name              VARCHAR(255)          NOT NULL,
    phone_number      VARCHAR(255)          NOT NULL,
    organization_code VARCHAR(255)          NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
);

ALTER TABLE user
    ADD CONSTRAINT UQ_USER_PHONE_NUMBER UNIQUE (phone_number);

-- 어드민 테이블
CREATE TABLE admin
(
    id       BIGINT AUTO_INCREMENT NOT NULL,
    created_dt datetime              NOT NULL,
    updated_dt datetime              NOT NULL,
    name     VARCHAR(255)          NOT NULL,
    email    VARCHAR(255)          NOT NULL,
    password VARCHAR(255)          NOT NULL,
    role     VARCHAR(255)          NOT NULL,
    CONSTRAINT pk_admin PRIMARY KEY (id)
);

ALTER TABLE admin
    ADD CONSTRAINT UQ_ADMIN_EMAIL UNIQUE (email);