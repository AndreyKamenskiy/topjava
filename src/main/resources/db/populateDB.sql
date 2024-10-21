-- noinspection SqlWithoutWhere
DELETE
FROM users; -- cascade deleting user_role and meals
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Guest', 'guest@gmail.com', 'guest');

INSERT INTO user_role (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals (user_id, date_time, calories, description)
VALUES (100000, '2024-10-20 09:51:00.000000', 500, 'завтрак юзера'),
       (100000, '2024-10-21 19:51:00.000000', 500, 'ужин юзера'),
       (100000, '2024-10-20 13:51:00.000000', 500, 'обед юзера'),
       (100001, '2024-10-20 12:00:00.000000', 500, 'смузи админа'),
       (100001, '2024-10-20 06:00:00.000000', 500, 'кофе админа'),
       (100001, '2024-10-20 21:30:00.000000', 500, 'успокоительное для админа');
