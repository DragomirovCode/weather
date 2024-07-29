CREATE TABLE Locations
(
    id        SERIAL PRIMARY KEY,
    name      VARCHAR(100),
    user_id   INTEGER,
    latitude  DECIMAL(40, 35),
    longitude DECIMAL(40, 35),
    FOREIGN KEY (user_id) REFERENCES Users (id),
    CONSTRAINT unique_lat_lon_user UNIQUE (latitude, longitude, user_id)
);
