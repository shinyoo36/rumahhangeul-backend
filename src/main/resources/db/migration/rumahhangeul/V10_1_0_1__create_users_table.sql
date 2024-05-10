CREATE TABLE IF NOT EXISTS users (
    id                                  BIGSERIAL,
    username                            VARCHAR(50)     NOT NULL UNIQUE,
    nama_depan                          VARCHAR(12)     NOT NULL,
    nama_belakang                       VARCHAR(12),
    password                            VARCHAR(20)     NOT NULL,
    email                               VARCHAR(100)    NOT NULL UNIQUE,
    border_used                         VARCHAR(20)     NOT NULL,
    profile_used                        VARCHAR(20)     NOT NULL,
    score                               BIGINT          DEFAULT 0,
    point                               BIGINT          DEFAULT 0,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS challenges (
    id                                  BIGSERIAL,
    user_id	                            BIGINT          NOT NULL,
    nama_challenge                      VARCHAR(20)     NOT NULL,
    first_clear                         VARCHAR(3)      NOT NULL,
    perfect_clear                       VARCHAR(3)      NOT NULL,
    gambar_url                          VARCHAR(20)     NOT NULL,
    PRIMARY KEY (id,
);

CREATE TABLE IF NOT EXISTS courses (
    id                                  BIGSERIAL,
    user_id	                            BIGINT          NOT NULL,
    nama_course                         VARCHAR(20)     NOT NULL,
    completed                           VARCHAR(3)      NOT NULL,
    gambar_url                          VARCHAR(20)     NOT NULL,
    PRIMARY KEY (id,
    );

CREATE TABLE IF NOT EXISTS items (
    id                                  BIGSERIAL,
    user_id	                            BIGINT          NOT NULL,
    tipe_item                           VARCHAR(20)     NOT NULL,
    nama_item                           VARCHAR(20)     NOT NULL,
    value_item                           VARCHAR(20)     NOT NULL,
    PRIMARY KEY (id,
);
