CREATE TABLE Sessions (
    id VARCHAR PRIMARY KEY,
    user_id INTEGER,
    expires_at TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(id)
)
