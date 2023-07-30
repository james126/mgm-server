--Postgres
DROP TABLE IF EXISTS contact;

CREATE TABLE IF NOT EXISTS contact (
   id BIGSERIAL PRIMARY KEY, --auto incrementing integer
   first_name VARCHAR(50) NOT NULL,
   last_name VARCHAR(50),
   email VARCHAR(50) NOT NULL,
   phone VARCHAR(50),
   address_line1 VARCHAR(50) NOT NULL,
   address_line2 VARCHAR(50) NOT NULL,
   message TEXT NOT NULL, --variable unlimited length
   update_datetime TIMESTAMP
);

-- Security
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS authorities;

CREATE TABLE IF NOT EXISTS users
(
    username VARCHAR(128) PRIMARY KEY,
    password VARCHAR(128) NOT NULL,
    enabled BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS authorities
(
    username  VARCHAR(128) NOT NULL,
    authority VARCHAR(128) NOT NULL
);

ALTER TABLE authorities DROP CONSTRAINT IF EXISTS authorities_unique;
ALTER TABLE authorities DROP CONSTRAINT IF EXISTS authorities_fk1;

ALTER TABLE authorities
    ADD CONSTRAINT authorities_unique UNIQUE (username, authority);
ALTER TABLE authorities
    ADD CONSTRAINT authorities_fk1 FOREIGN KEY (username) REFERENCES users (username);
