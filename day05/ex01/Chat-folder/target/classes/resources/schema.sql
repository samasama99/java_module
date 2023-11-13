CREATE TABLE
    users
(
    id       bigserial PRIMARY KEY,
    login    VARCHAR(50) unique NOT NULL,
    password VARCHAR(50)        NOT NULL
);

CREATE TABLE
    rooms
(
    id       serial PRIMARY KEY,
    name     VARCHAR(50) unique NOT NULL,
    owner_id BIGINT             NOT NULL,
    FOREIGN KEY (owner_id) REFERENCES users (id)
);

CREATE TABLE
    users_rooms
(
    user_id BIGINT,
    room_id BIGINT,
    PRIMARY KEY (user_id, room_id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (room_id) REFERENCES rooms (id)
);


CREATE TABLE messages
(
    id        bigserial PRIMARY KEY,
    message   TEXT NOT NULL,
    date      TIMESTAMP,
    author_id BIGINT,
    room_id   BIGINT,
    FOREIGN KEY (author_id) REFERENCES users (id),
    FOREIGN KEY (room_id) REFERENCES rooms (id)
);
