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
    CONSTRAINT fk_book_author FOREIGN KEY (author_id) references author (author_id)
);

CREATE TABLE exemplar
(
    exemplar_id               integer AUTO_INCREMENT PRIMARY KEY,
    exemplar_borrowable       bit,
    exemplar_condition        varchar(255),
    exemplar_inventory_number integer,
    of_book_id                integer,
    exemplar_deleted          bit,
    constraint fk_exemplar_book foreign key (of_book_id) references book (book_id)
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
    borowing_id        integer AUTO_INCREMENT PRIMARY KEY,
    borowing_active    bit,
    borowing_from_date date,
    borowing_to_date   date,
    exemplar_id        integer,
    user_id            integer,
    constraint fk_borrowing_user foreign key (user_id) references user (user_id),
    constraint fk_borrowing_exemplar foreign key (exemplar_id) references exemplar (exemplar_id)
);