INSERT INTO users (id, name, email, password) VALUES
  (100000, 'Admin', 'admin@gmail.com', '{noop}admin'),
  (100001, 'Alex', 'alex@yandex.ru', '{noop}password');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_ADMIN', 100000),
  ('ROLE_USER', 100001);