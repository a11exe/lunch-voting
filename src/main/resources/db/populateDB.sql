
-- noinspection SqlResolveForFile

DELETE FROM user_roles;
DELETE FROM vote;
DELETE FROM menu;
DELETE FROM restaurant;
DELETE FROM users;

INSERT INTO users (id, name, email, password) VALUES
  (100000, 'Admin', 'admin@gmail.com', '{noop}admin'),
  (100001, 'User', 'user@yandex.ru', '{noop}password'),
  (100002, 'Bob', 'bob@yandex.ru', '{noop}password'),
  (100003, 'Jhon', 'jhon@yandex.ru', '{noop}password');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_ADMIN', 100000),
  ('ROLE_USER', 100001),
  ('ROLE_USER', 100002),
  ('ROLE_USER', 100003);

INSERT INTO restaurant (id, name, description) VALUES
  (100004, 'MCDonalds', 'mc ddd'),
  (100005, 'KFC', 'chicken'),
  (100006, 'Burger King', 'big burgers'),
  (100007, 'Il Patio', 'pizza');

INSERT INTO menu (id, date, description, restaurant_id) VALUES
  (100008, '2015-05-30', 'burger 100; coffee 200; potato 50', 100004),
  (100009, '2015-05-30', 'chicken roll 100; black burger 200; potato 50', 100005),
  (100010, '2015-05-30', 'max burger 100; chicken mix 200; cocktail 50', 100006),
  (100011, '2015-06-30', 'pizza 100; cesar 200; coffee 50', 100007);

INSERT INTO vote (id, date, menu_id, user_id)
VALUES (100012, '2015-05-30', 100008, 100000),
       (100013, '2015-05-30', 100009, 100001),
       (100014, '2015-05-30', 100010, 100002),
       (100015, '2015-06-30', 100011, 100003);
