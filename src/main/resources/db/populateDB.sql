DELETE FROM user_roles;
DELETE FROM vote;
DELETE FROM menu;
DELETE FROM restaurant;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (id, name, email, password) VALUES
  (100000, 'Admin', 'admin@gmail.com', 'admin'),
  (100001, 'Alex', 'alex@yandex.ru', 'password'),
  (100002, 'Bob', 'bob@yandex.ru', 'password'),
  (100003, 'Jhon', 'jhon@yandex.ru', 'password');

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
  (100008, '2015-05-30', 'burger 100; coffe 200; potato 50', 100004),
  (100009, '2015-05-30', 'chiken roll 100; black burger 200; potato 50', 100005),
  (100010, '2015-05-30', 'max burger 100; chiken mix 200; cocatail 50', 100006),
  (100011, '2015-06-30', 'pizza 100; cesar 200; coffe 50', 100007);

INSERT INTO vote (id, date, menu_id, user_id)
VALUES (100012, '2015-05-30', 100008, 100000),
       (100013, '2015-05-30', 100009, 100001),
       (100014, '2015-05-30', 100010, 100002),
       (100015, '2015-05-30', 100011, 100003);
