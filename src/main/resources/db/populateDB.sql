DELETE FROM user_roles;
DELETE FROM votes;
DELETE FROM menus;
DELETE FROM restaurants;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password) VALUES
  ('Admin', 'admin@gmail.com', 'admin'),
  ('Alex', 'alex@yandex.ru', 'password'),
  ('Bob', 'bob@yandex.ru', 'password'),
  ('Jhon', 'jhon@yandex.ru', 'password');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_ADMIN', 100000),
  ('ROLE_USER', 100001),
  ('ROLE_USER', 100002),
  ('ROLE_USER', 100003);

INSERT INTO restaurants (name, description) VALUES
  ('MCDonalds', 'mc ddd'),
  ('KFC', 'chicken'),
  ('Burger King', 'big burgers'),
  ('Il Patio', 'pizza');

INSERT INTO menus (date, description, restaurant_id) VALUES
  ('2015-05-30 10:00:00', 'burger 100; coffe 200; potato 50', 100004),
  ('2015-05-30 10:00:00', 'chiken roll 100; black burger 200; potato 50', 100005),
  ('2015-05-30 10:00:00', 'max burger 100; chiken mix 200; cocatail 50', 100006),
  ('2015-05-30 10:00:00', 'pizza 100; cesar 200; coffe 50', 100007);

INSERT INTO votes (date_time, menu_id, user_id)
VALUES ('2015-05-30 10:00:00', 100008, 100000),
       ('2015-05-30 10:00:00', 100009, 100001),
       ('2015-05-30 10:00:00', 100010, 100002),
       ('2015-05-30 10:00:00', 100010, 100003);
