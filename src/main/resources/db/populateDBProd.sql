-- -- populate db
insert into users (id, name, email, password) select id, name, email, password
                  from (
                         select
                           100000 as id,
                           'Admin' as name,
                           'admin@gmail.com' as email,
                           '{noop}admin' as password
                         union
                         select
                           100001,
                           'User',
                           'user@yandex.ru',
                           '{noop}password'
                       ) x
                  where not exists(select id, name, email, password
                                   from users);
-- INSERT INTO users (id, name, email, password) VALUES
--   (100000, 'Admin', 'admin@gmail.com', '{noop}admin'),
--   (100001, 'Alex', 'alex@yandex.ru', '{noop}password');

insert into user_roles (role, user_id) select role, user_id
    from (
      select
      'ROLE_ADMIN' as role,
      100000 as user_id
      union
      select
      'ROLE_USER',
      100001
    ) x
    where not exists(select role, user_id from user_roles);


-- INSERT INTO user_roles (role, user_id) VALUES
--   ('ROLE_ADMIN', 100000),
--   ('ROLE_USER', 100001);