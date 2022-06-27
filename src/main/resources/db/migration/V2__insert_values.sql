INSERT INTO author(author_name, author_deleted) VALUES ('Emma Writer', false);
INSERT INTO author(author_name, author_deleted) VALUES ('Margaret Wise Brown', false);
INSERT INTO author(author_name, author_deleted) VALUES ('Anna Martha', false);
INSERT INTO author(author_name, author_deleted) VALUES ('Colleen Hoover', false);
INSERT INTO author(author_name, author_deleted) VALUES ('Dean Koontz', false);
INSERT INTO author(author_name, author_deleted) VALUES ('Elle Marr', false);
INSERT INTO author(author_name, author_deleted) VALUES ('Vivian Wood', false);
INSERT INTO author(author_name, author_deleted) VALUES ('Ashley Farley', false);

INSERT INTO user(user_address, user_deleted, user_email, user_name, user_phone_number) VALUES
                ('Main street, Nr.7',false,'emil.white@gmail.com','Emil White','3245446656');
INSERT INTO user(user_address, user_deleted, user_email, user_name, user_phone_number) VALUES
                ('Flower street, Nr.21',false,'ana.brown@gmail.com','Ana Brown','3277765488');
INSERT INTO user(user_address, user_deleted, user_email, user_name, user_phone_number) VALUES
                ('Liberty street, Nr. 12',false,'tina.morgen@gmail.com','Tina Morgen','3677884654');
INSERT INTO user(user_address, user_deleted, user_email, user_name, user_phone_number) VALUES
                ('Willian street, Nr. 3',false,'mary.davis@gmail.com','Mary Davis','3576548876');
INSERT INTO user(user_address, user_deleted, user_email, user_name, user_phone_number) VALUES
                ('Blane street, Nr. 67',false,'james.wacker@gmail.com','James Wacker','4355898711');


INSERT INTO book(book_isbn, book_deleted, book_genre, number_of_pages, book_publisher, book_publishing_year, book_title,
                 author_id) VALUES ('6245874512',false,'CHILDREN',123,'Johnson',2000,'Adventures in Paris',1);
INSERT INTO book(book_isbn, book_deleted, book_genre, number_of_pages, book_publisher, book_publishing_year, book_title,
                 author_id) VALUES ('6243456557',false,'CHILDREN',212,'The Best',2001,'Tom Connor',1);
INSERT INTO book(book_isbn, book_deleted, book_genre, number_of_pages, book_publisher, book_publishing_year, book_title,
                 author_id) VALUES ('4323434343',false,'CHILDREN',312,'Thomson',2019,'Goodnight Moon',2);
INSERT INTO book(book_isbn, book_deleted, book_genre, number_of_pages, book_publisher, book_publishing_year, book_title,
                 author_id) VALUES ('4365465657',false,'CHILDREN',218,'Thomson',2020,'Making dreams come true',3);
INSERT INTO book(book_isbn, book_deleted, book_genre, number_of_pages, book_publisher, book_publishing_year, book_title,
                 author_id) VALUES ('6734656534',false,'FANTASY_AND_SF',222,'Bertelsmann',2020,'Verity',4);
INSERT INTO book(book_isbn, book_deleted, book_genre, number_of_pages, book_publisher, book_publishing_year, book_title,
                 author_id) VALUES ('4357787432',false,'FANTASY_AND_SF',245,'Bertelsmann',2017,'Quicksilver',5);
INSERT INTO book(book_isbn, book_deleted, book_genre, number_of_pages, book_publisher, book_publishing_year, book_title,
                 author_id) VALUES ('5557678453',false,'FANTASY_AND_SF',145,'Bertelsmann',2018,'Strangers we know',6);
INSERT INTO book(book_isbn, book_deleted, book_genre, number_of_pages, book_publisher, book_publishing_year, book_title,
                 author_id) VALUES ('2346548643',false,'ROMANCE',256,'The Best',2020,'The sinner',7);
INSERT INTO book(book_isbn, book_deleted, book_genre, number_of_pages, book_publisher, book_publishing_year, book_title,
                 author_id) VALUES ('3453457675',false,'ROMANCE',243,'Thomson',2021,'Act of mercy',8);
INSERT INTO book(book_isbn, book_deleted, book_genre, number_of_pages, book_publisher, book_publishing_year, book_title,
                 author_id) VALUES ('3656565686',false,'ROMANCE',277,'The Best',2019,'Dream big, Stella!',7);
INSERT INTO book(book_isbn, book_deleted, book_genre, number_of_pages, book_publisher, book_publishing_year, book_title,
                 author_id) VALUES ('7643454334',false,'ROMANCE',123,'Thomson',2018,'After Hello',8);


INSERT INTO exemplar(exemplar_borrowable, exemplar_condition, exemplar_deleted, exemplar_inventory_number, of_book_id)
                VALUES (true,'NEW',false,123,1);
INSERT INTO exemplar(exemplar_borrowable, exemplar_condition, exemplar_deleted, exemplar_inventory_number, of_book_id)
                VALUES (true,'GOOD',false,124,3);
INSERT INTO exemplar(exemplar_borrowable, exemplar_condition, exemplar_deleted, exemplar_inventory_number, of_book_id)
                VALUES (true,'GOOD',false,125,4);
INSERT INTO exemplar(exemplar_borrowable, exemplar_condition, exemplar_deleted, exemplar_inventory_number, of_book_id)
                VALUES (true,'USED',false,126,5);
INSERT INTO exemplar(exemplar_borrowable, exemplar_condition, exemplar_deleted, exemplar_inventory_number, of_book_id)
                VALUES (true,'NEW',false,127,6);
INSERT INTO exemplar(exemplar_borrowable, exemplar_condition, exemplar_deleted, exemplar_inventory_number, of_book_id)
                VALUES (true,'USED',false,128,7);
INSERT INTO exemplar(exemplar_borrowable, exemplar_condition, exemplar_deleted, exemplar_inventory_number, of_book_id)
                VALUES (true,'GOOD',false,129,2);
INSERT INTO exemplar(exemplar_borrowable, exemplar_condition, exemplar_deleted, exemplar_inventory_number, of_book_id)
                VALUES (true,'GOOD',false,130,4);
INSERT INTO exemplar(exemplar_borrowable, exemplar_condition, exemplar_deleted, exemplar_inventory_number, of_book_id)
                VALUES (true,'USED',false,131,5);
INSERT INTO exemplar(exemplar_borrowable, exemplar_condition, exemplar_deleted, exemplar_inventory_number, of_book_id)
                VALUES (true,'NEW',false,132,8);
INSERT INTO exemplar(exemplar_borrowable, exemplar_condition, exemplar_deleted, exemplar_inventory_number, of_book_id)
                VALUES (true,'GOOD',false,133,9);
INSERT INTO exemplar(exemplar_borrowable, exemplar_condition, exemplar_deleted, exemplar_inventory_number, of_book_id)
                VALUES (true,'GOOD',false,134,10);
INSERT INTO exemplar(exemplar_borrowable, exemplar_condition, exemplar_deleted, exemplar_inventory_number, of_book_id)
                VALUES (true,'USED',false,135,8);
INSERT INTO exemplar(exemplar_borrowable, exemplar_condition, exemplar_deleted, exemplar_inventory_number, of_book_id)
                VALUES (true,'NEW',false,136,9);
INSERT INTO exemplar(exemplar_borrowable, exemplar_condition, exemplar_deleted, exemplar_inventory_number, of_book_id)
                VALUES (true,'USED',false,137,10);
INSERT INTO exemplar(exemplar_borrowable, exemplar_condition, exemplar_deleted, exemplar_inventory_number, of_book_id)
                VALUES (true,'GOOD',false,138,11);
INSERT INTO exemplar(exemplar_borrowable, exemplar_condition, exemplar_deleted, exemplar_inventory_number, of_book_id)
                VALUES (true,'GOOD',false,139,11);
INSERT INTO exemplar(exemplar_borrowable, exemplar_condition, exemplar_deleted, exemplar_inventory_number, of_book_id)
                VALUES (true,'USED',false,140,3);
