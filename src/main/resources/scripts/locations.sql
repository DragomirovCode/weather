CREATE TABLE Locations (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(100),
    user_id INTEGER,
    latitude DECIMAL(6),
    longitude DECIMAL(6),
    FOREIGN KEY (user_id) REFERENCES Users(id)
)
