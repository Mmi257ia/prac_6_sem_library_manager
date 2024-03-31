DROP TABLE IF EXISTS author, publisher, product, book, reader, issue CASCADE;
DROP FUNCTION IF EXISTS product_authors_checking_func CASCADE;
-- DROP ROLE IF EXISTS website_backend;

-- CREATE ROLE website_backend WITH
--   LOGIN
--   SUPERUSER
--   INHERIT
--   CREATEDB
--   CREATEROLE
--   REPLICATION
--   PASSWORD '12345678';

CREATE TABLE author (
    author_id serial PRIMARY KEY,
    "name" varchar(100) NOT NULL
);

CREATE TABLE publisher (
    publisher_id serial PRIMARY KEY,
    "name" varchar(100) NOT NULL
);

CREATE TABLE product (
    product_id serial PRIMARY KEY,
    isbn varchar(13),
    "name" text NOT NULL,
    authors integer[],
    publisher_id integer REFERENCES publisher ON DELETE SET NULL ON UPDATE CASCADE,
    year_of_publishing integer
);

CREATE TABLE book (
    book_id serial PRIMARY KEY,
    product_id integer REFERENCES product ON DELETE CASCADE ON UPDATE CASCADE NOT NULL,
    receiving_date date
);

CREATE TABLE reader (
    reader_id serial PRIMARY KEY,
    "name" varchar(100),
    address text,
    phone bigint
);

CREATE TABLE issue (
    issue_id serial PRIMARY KEY,
    book_id integer REFERENCES book ON DELETE RESTRICT ON UPDATE CASCADE NOT NULL,
    reader_id integer REFERENCES reader ON DELETE RESTRICT ON UPDATE CASCADE NOT NULL,
    issued date NOT NULL,
    returned date,
    deadline date
);

CREATE OR REPLACE FUNCTION product_authors_checking_func()
    RETURNS trigger
    AS $func$
        DECLARE
            authors_array integer[] := (SELECT array_agg(author_id) FROM author);
        BEGIN
            IF NOT NEW.authors <@ authors_array THEN
                RAISE EXCEPTION 'Invalid authors IDs specified: %', NEW.authors;
            END IF;
            RETURN NULL;
        END;
    $func$
LANGUAGE plpgsql;

CREATE CONSTRAINT TRIGGER product_authors_checking
    AFTER INSERT OR UPDATE OF authors ON product
    DEFERRABLE INITIALLY IMMEDIATE
    FOR EACH ROW
    EXECUTE FUNCTION product_authors_checking_func();