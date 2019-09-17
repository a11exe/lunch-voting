DROP TABLE IF EXISTS vote;
DROP TABLE IF EXISTS menu;
DROP TABLE IF EXISTS restaurant;
DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS users;
-- DELETE FROM INFORMATION_SCHEMA.SEQUENCES;

CREATE TABLE users
(
  id               INTEGER AUTO_INCREMENT PRIMARY KEY,
  name             VARCHAR                 NOT NULL,
  email            VARCHAR                 NOT NULL,
  password         VARCHAR                 NOT NULL,
);
CREATE UNIQUE INDEX users_unique_email_idx ON users (email);

CREATE TABLE user_roles
(
  user_id INTEGER AUTO_INCREMENT PRIMARY KEY,
  role    VARCHAR,
  CONSTRAINT user_roles_idx UNIQUE (user_id, role),
  FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE restaurant (
  id          INTEGER AUTO_INCREMENT PRIMARY KEY,
  name        VARCHAR   NOT NULL,
  description TEXT      NOT NULL,
  CONSTRAINT name_idx UNIQUE (name)
);

CREATE TABLE menu (
  id                    INTEGER AUTO_INCREMENT PRIMARY KEY,
  date                  DATE DEFAULT now() NOT NULL,
  description           TEXT      NOT NULL,
  restaurant_id         INTEGER NOT NULL,
  CONSTRAINT date_restaurant_idx UNIQUE (date, restaurant_id),
  FOREIGN KEY (restaurant_id) REFERENCES restaurant (id) ON DELETE CASCADE
);

CREATE TABLE vote (
  id                    INTEGER AUTO_INCREMENT PRIMARY KEY,
  date                  DATE DEFAULT now() NOT NULL,
  menu_id               INTEGER NOT NULL,
  user_id               INTEGER NOT NULL,
  CONSTRAINT user_date_idx UNIQUE (user_id, date),
  FOREIGN KEY (menu_id) REFERENCES menu (id) ON DELETE CASCADE,
  FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);