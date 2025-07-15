START TRANSACTION;

ALTER TABLE user_roles
    DROP CONSTRAINT fk_user_roles_on_user_entity;

ALTER TABLE user_roles
    ADD CONSTRAINT fk_user_roles_on_user_entity FOREIGN KEY (user_id) REFERENCES users (id)
    ON DELETE CASCADE;

    DELETE FROM users;
    DELETE FROM user_roles;

    ALTER TABLE users
        ADD email VARCHAR(255);

    ALTER TABLE users
        ALTER COLUMN email SET NOT NULL;

    ALTER TABLE users
    DROP COLUMN username;
COMMIT;
