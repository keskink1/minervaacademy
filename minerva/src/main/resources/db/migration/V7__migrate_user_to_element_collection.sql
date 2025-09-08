DROP TABLE IF EXISTS user_roles;

CREATE TABLE user_roles (
                            user_id BIGINT NOT NULL,
                            role_name VARCHAR(255) NOT NULL,

                            CONSTRAINT fk_user_roles_user_id FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,

                            UNIQUE (user_id, role_name)
);