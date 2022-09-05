CREATE TABLE IF NOT EXISTS users
(
    id         BIGSERIAL PRIMARY KEY,
    login      VARCHAR(128) UNIQUE NOT NULL,
    first_name VARCHAR(128)        NOT NULL,
    last_name  VARCHAR(128)        NOT NULL,
    role       VARCHAR(16)         NOT NULL
)