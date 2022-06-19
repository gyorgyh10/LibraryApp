CREATE TABLE author
(
    author_id      integer AUTO_INCREMENT PRIMARY KEY,
    author_name    varchar(255),
    author_deleted bit
);

CREATE TABLE book
(
    book_id              integer AUTO_INCREMENT PRIMARY KEY,
    book_isbn            varchar(255),
    book_genre           varchar(255),
    number_of_pages      integer,
    book_publisher       varchar(255),
    book_publishing_year integer,
    book_title           varchar(255),
    author_id            integer,
    book_deleted         bit,
    CONSTRAINT fk_book_author FOREIGN KEY (author_id) REFERENCES author (author_id)
);

CREATE TABLE exemplar
(
    exemplar_id               integer AUTO_INCREMENT PRIMARY KEY,
    exemplar_borrowable       bit,
    exemplar_condition        varchar(255),
    exemplar_inventory_number integer,
    of_book_id                integer,
    exemplar_deleted          bit,
    CONSTRAINT fk_exemplar_book FOREIGN KEY (of_book_id) REFERENCES book (book_id)
);

CREATE TABLE user
(
    user_id           integer AUTO_INCREMENT PRIMARY KEY,
    user_address      varchar(255),
    user_email        varchar(255),
    user_name         varchar(255),
    user_phone_number varchar(255),
    user_deleted      bit
);

CREATE TABLE borrowing
(
    borrowing_id        integer AUTO_INCREMENT PRIMARY KEY,
    borrowing_active    bit,
    borrowing_from_date date,
    borrowing_to_date   date,
    exemplar_id        integer,
    user_id            integer,
    CONSTRAINT fk_borrowing_user FOREIGN KEY (user_id) REFERENCES user (user_id),
    CONSTRAINT fk_borrowing_exemplar FOREIGN KEY (exemplar_id) REFERENCES exemplar (exemplar_id)
);