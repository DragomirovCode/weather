CREATE TABLE Users (
    id INTEGER PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    login VARCHAR(100) UNIQUE,
    password VARCHAR(100) NOT NULL
)