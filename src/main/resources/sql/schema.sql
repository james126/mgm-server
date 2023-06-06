CREATE TABLE IF NOT EXISTS contact (
   id BIGSERIAL PRIMARY KEY, --auto incrementing integer
   first_name VARCHAR(50) NOT NULL,
   last_name VARCHAR(50),
   email VARCHAR(50) NOT NULL,
   phone VARCHAR(50),
   address_line1 VARCHAR(50) NOT NULL,
   address_line2 VARCHAR(50) NOT NULL,
   message TEXT NOT NULL, --variable unlimited length
   update_date_time TIMESTAMP
);
