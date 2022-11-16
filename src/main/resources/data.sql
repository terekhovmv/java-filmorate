DELETE FROM film_genres;
DELETE FROM likes;
DELETE FROM films;
DELETE FROM friendship;
DELETE FROM users;

ALTER TABLE users
    ALTER COLUMN id RESTART WITH 1;

ALTER TABLE films
    ALTER COLUMN id RESTART WITH 1;

MERGE INTO mpa (id, name) KEY(id)
    VALUES (1, 'G'),
           (2, 'PG'),
           (3, 'PG-13'),
           (4, 'R'),
           (5, 'NC-17');

MERGE INTO genres (id, name) KEY(id)
    VALUES (1, 'Комедия'),
           (2, 'Драма'),
           (3, 'Мультфильм'),
           (4, 'Боевик'),
           (5, 'Приключения'),
           (6, 'Биография');
