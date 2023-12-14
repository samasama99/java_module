INSERT INTO users (login, password)
VALUES ('user1', 'password1'),
       ('user2', 'password2'),
       ('user3', 'password3'),
       ('user4', 'password4'),
       ('user5', 'password5');

INSERT INTO rooms (name, owner_id)
VALUES ('room1', 1),
       ('room2', 2),
       ('room3', 3),
       ('room6', 3),
       ('room4', 4),
       ('room7', 4),
       ('room8', 4),
       ('room5', 5);

INSERT INTO users_rooms (user_id, room_id)
VALUES (1, 1),
       (1, 5),
       (2, 2),
       (3, 3),
       (3, 2),
       (3, 4),
       (4, 4),
       (5, 5),
       (5, 1),
       (5, 3);

INSERT INTO messages (message, date, author_id, room_id)
VALUES ('Message 1', CURRENT_TIMESTAMP, 1, 1),
       ('Message 2', CURRENT_TIMESTAMP, 2, 2),
       ('Message 3', CURRENT_TIMESTAMP, 3, 3),
       ('Message 4', CURRENT_TIMESTAMP, 4, 4),
       ('Message 5', CURRENT_TIMESTAMP, 5, 5);

