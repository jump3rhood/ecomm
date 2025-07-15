

ALTER TABLE users
    ADD  COLUMN provider VARCHAR(50) DEFAULT  'LOCAL',
    ADD COLUMN provider_id VARCHAR(255);

ALTER TABLE users
    ALTER COLUMN password DROP NOT NULL ;

ALTER TABLE users
    ADD CONSTRAINT uk_users_provider_provider_id UNIQUE(provider, provider_id);