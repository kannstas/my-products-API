CREATE TABLE products
(
    id          UUID PRIMARY KEY,
    title       VARCHAR(255) UNIQUE NOT NULL,
    description VARCHAR(4096)       NOT NULL,
    price       NUMERIC DEFAULT 0 CHECK (price >= 0),
    quantity    INTEGER DEFAULT 0
);

-- add comments to tables and columns

CREATE TABLE receipts
(
    id             UUID PRIMARY KEY,
    document_title VARCHAR(255) NOT NULL,
    product_id     UUID         NOT NULL REFERENCES products (id),
    quantity       INTEGER CHECK ( quantity > 0 )
);


CREATE TABLE sales
(
    id             UUID PRIMARY KEY,
    document_title VARCHAR(255) NOT NULL,
    product_id     UUID         NOT NULL REFERENCES products (id),
    quantity       INTEGER      NOT NULL CHECK ( quantity > 0 ),
    cost           NUMERIC      NOT NULL
);

