-- 1. Tabela Użytkowników
CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       email VARCHAR(255) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       name VARCHAR(100),
                       age INT,
                       gender VARCHAR(20),
                       city VARCHAR(100),
                       bio TEXT,
                       profile_image_url VARCHAR(500),
                       preferred_gender VARCHAR(20),
                       preferred_min_age INT DEFAULT 18,
                       preferred_max_age INT DEFAULT 99,
                       preferred_city VARCHAR(100)
);

-- 2. Tabela Polubień z KASKADĄ
CREATE TABLE user_likes (
                            id BIGSERIAL PRIMARY KEY,
                            liker_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
                            liked_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. Tabela Wiadomości z KASKADĄ
CREATE TABLE chat_messages (
                               id BIGSERIAL PRIMARY KEY,
                               sender_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
                               recipient_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
                               content TEXT NOT NULL,
                               sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);