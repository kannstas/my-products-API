CREATE TABLE products
(
    id          UUID PRIMARY KEY,
    title       VARCHAR(255)  NOT NULL,
    description VARCHAR(4096) NOT NULL,
    price       NUMERIC DEFAULT 0 CHECK (price >= 0),
    is_stock    BOOL    DEFAULT FALSE
);