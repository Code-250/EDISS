-- Use the database
USE bookstore;

-- Create tables if they don't exist
-- Note: MySQL already creates the database from the environment variables

CREATE TABLE IF NOT EXISTS books (
                                     isbn VARCHAR(20) PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    genre VARCHAR(100) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    quantity INT NOT NULL
    );

CREATE TABLE IF NOT EXISTS customers (
                                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                         user_id VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    address VARCHAR(255) NOT NULL,
    address2 VARCHAR(255),
    city VARCHAR(100) NOT NULL,
    state CHAR(2) NOT NULL,
    zipcode VARCHAR(20) NOT NULL
    );

-- Grant privileges to bookstore user
CREATE USER IF NOT EXISTS 'bookstore'@'%' IDENTIFIED BY 'bookstore';
GRANT ALL PRIVILEGES ON bookstore.* TO 'bookstore'@'%';
FLUSH PRIVILEGES;

-- Insert sample data (optional)
INSERT INTO books (isbn, title, author, description, genre, price, quantity)
VALUES
    ('9781234567897', 'Spring Boot in Action', 'Craig Walls', 'A comprehensive guide to Spring Boot', 'Technology', 39.99, 25),
    ('9780123456789', 'Docker for Developers', 'John Doe', 'Learn Docker from scratch', 'Technology', 29.99, 30);

INSERT INTO customers (user_id, name, phone, address, city, state, zipcode)
VALUES
    ('john.doe@example.com', 'John Doe', '555-123-4567', '123 Main St', 'New York', 'NY', '10001'),
    ('jane.smith@example.com', 'Jane Smith', '555-987-6543', '456 Oak Ave', 'Chicago', 'IL', '60601');