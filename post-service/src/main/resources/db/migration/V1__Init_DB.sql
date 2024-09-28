CREATE TABLE post (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    anons VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
);

CREATE TABLE post_author (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL
);