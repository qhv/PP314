CREATE TABLE IF NOT EXISTS users
(
    id         BIGSERIAL PRIMARY KEY,
    login      VARCHAR(128) UNIQUE NOT NULL,
    password   VARCHAR(128)        NOT NULL,
    first_name VARCHAR(128)        NOT NULL,
    last_name  VARCHAR(128)        NOT NULL,
);

CREATE TABLE IF NOT EXISTS role
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(32) NOT NULL
);

CREATE TABLE IF NOT EXISTS users_role
(
    user_id BIGINT,
    role_id INT,
    PRIMARY KEY (user_id, role_id)
)