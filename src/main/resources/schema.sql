CREATE TABLE IF NOT EXISTS users (
    id            bigint      GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email         varchar     UNIQUE NOT NULL,
    login         varchar     UNIQUE NOT NULL,
    name          varchar     UNIQUE NOT NULL,
    birthday      date        NOT NULL
);

CREATE TABLE IF NOT EXISTS friendship (
    user_id       bigint      NOT NULL REFERENCES users (id),
    friend_id     bigint      NOT NULL REFERENCES users (id),
    confirmed     boolean     NOT NULL,

    CONSTRAINT pk_friendship PRIMARY KEY (
        user_id,
        friend_id
    )
);

CREATE TABLE IF NOT EXISTS mpa_ratings (
    id            smallint    GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name          varchar     NOT NULL
);

CREATE TABLE IF NOT EXISTS films (
    id            int         GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name          varchar     NOT NULL,
    description   varchar,
    release_date  date,
    duration      int,
    mpa_rating_id smallint    REFERENCES mpa_ratings (id)
);

CREATE TABLE IF NOT EXISTS likes (
    film_id       int         NOT NULL REFERENCES films (id),
    user_id       bigint      NOT NULL REFERENCES users (id),

    CONSTRAINT pk_like PRIMARY KEY (
        film_id,
        user_id
    )
);

CREATE TABLE IF NOT EXISTS genres (
    id            smallint    GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name          varchar     NOT NULL
);

CREATE TABLE IF NOT EXISTS film_genres (
    film_id       int         NOT NULL REFERENCES users (id),
    genre_id      smallint    NOT NULL REFERENCES genres (id),

    CONSTRAINT pk_film_genre PRIMARY KEY (
        film_id,
        genre_id
    )
);
