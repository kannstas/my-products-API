CREATE TABLE products (
    id UUID PRIMARY KEY,
    title VARCHAR (255) NOT NULL CHECK (LENGTH(title) <= 255),
    description varchar not null check (LENGTH(description) <= 4096 ),
    price NUMERIC DEFAULT 0 CHECK (price >= 0),
    is_stock BOOL DEFAULT FALSE
);