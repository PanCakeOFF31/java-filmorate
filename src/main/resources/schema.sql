CREATE TYPE event_type_enum AS ENUM ('LIKE', 'REVIEW', 'FRIEND');
CREATE TYPE operation_enum AS ENUM ('ADD', 'REMOVE', 'UPDATE');

CREATE TABLE IF NOT EXISTS mpa (
    id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar(10) NOT NULL UNIQUE CHECK(TRIM(name) <> '')
);

CREATE TABLE IF NOT EXISTS  genre (
    id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar(32) NOT NULL UNIQUE CHECK(TRIM(name) <> '')
);

CREATE TABLE IF NOT EXISTS  director (
    id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar(128) NOT NULL CHECK(TRIM(name) <> '')
);

CREATE TABLE IF NOT EXISTS  film (
    id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar(128) NOT NULL CHECK(TRIM(name) <> ''),
    description varchar(200) NOT NULL,
    release_date date NOT NULL CHECK (release_date > '1895-12-28'),
    duration int NOT NULL CHECK (duration > 0),
    mpa int REFERENCES mpa(id)
);

CREATE TABLE IF NOT EXISTS  film_genre (
    film_id int REFERENCES film(id) ON DELETE CASCADE,
    genre_id int REFERENCES genre(id) ON DELETE CASCADE,
    UNIQUE (film_id, genre_id)
);

CREATE TABLE IF NOT EXISTS  film_director (
    film_id int REFERENCES film(id) ON DELETE CASCADE,
    director_id int REFERENCES director(id) ON DELETE CASCADE,
    UNIQUE (film_id, director_id)
);

CREATE TABLE IF NOT EXISTS  person (
    id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email varchar(128) NOT NULL CHECK(email ~ ('^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+[.][A-Za-z]+$')) UNIQUE,
    login varchar(64) NOT NULL CHECK(TRIM(name) <> '') UNIQUE,
    name varchar(64) NULL,
    birthday date CHECK (birthday < NOW())
);

CREATE TABLE IF NOT EXISTS  friendship (
    user_id int REFERENCES person(id) ON DELETE CASCADE,
    friend_id int REFERENCES person(id) ON DELETE CASCADE,
    is_confirmed boolean DEFAULT false,
    UNIQUE(user_id, friend_id),
    CHECK (user_id <> friend_id)
);

CREATE TABLE IF NOT EXISTS  film_like (
        film_id int REFERENCES film(id) ON DELETE CASCADE,
        user_id int REFERENCES person(id) ON DELETE CASCADE,
        UNIQUE(film_id, user_id)
);

CREATE TABLE IF NOT EXISTS  film_review (
    id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    user_id int REFERENCES person(id) ON DELETE CASCADE,
    film_id int REFERENCES film(id) ON DELETE CASCADE,
    content varchar(1000) NOT NULL CHECK(TRIM(content) <> ''),
    is_positive boolean NOT NULL,
    UNIQUE (user_id, film_id)
);

CREATE TABLE IF NOT EXISTS  review_like (
    review_id int REFERENCES film_review(id) ON DELETE CASCADE,
    user_id int REFERENCES person(id) ON DELETE CASCADE,
    is_like boolean NOT NULL,
    UNIQUE (review_id, user_id)
);

CREATE TABLE IF NOT EXISTS  film_rate (
    film_id int REFERENCES film(id) ON DELETE CASCADE,
    user_id int REFERENCES person(id) ON DELETE CASCADE,
    user_rate int CHECK (user_rate BETWEEN 1 AND 10),
    UNIQUE (film_id, user_id)
);

CREATE TABLE IF NOT EXISTS  event (
    id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    time_stamp bigint NOT NULL,
    user_id int REFERENCES person(id) ON DELETE CASCADE,
    event_type event_type_enum,
    operation operation_enum,
    entity_id int NOT NULL CHECK(entity_id > 0)
);

DELETE FROM film;
DELETE FROM person;
DELETE FROM mpa;
DELETE FROM genre;
DELETE FROM director;
DELETE FROM film_genre;
DELETE FROM film_director;
DELETE FROM friendship;
DELETE FROM film_like;
DELETE FROM film_review;
DELETE FROM review_like;
DELETE FROM film_rate;
DELETE FROM event;

ALTER TABLE mpa ALTER id RESTART;
ALTER TABLE genre ALTER id RESTART;
ALTER TABLE director ALTER id RESTART;
ALTER TABLE film ALTER id RESTART;
ALTER TABLE person ALTER id RESTART;
ALTER TABLE film_review ALTER id RESTART;
ALTER TABLE event ALTER id RESTART;