ALTER TABLE event
    ADD COLUMN reward_limit_per_user INT NULL; -- NULL이면 무제한
