# LibraryApp

This is an application, that helps to register the borrowings of a library. 

##Entities used

* Author 
* Book
* Exemplar
* User
* Borrowing


## API endpoints

### Author

* Save an author: (POST) /api/library/authors
* Find an author by id: (GET) /api/library/authors/{authorId}
* List all authors: (GET) /api/library/authors
* Delete an author by id: (DELETE) /api/library/authors/{authorId}
* Update an author by id: (PUT) /api/library/authors/{authorId}
* List all books of the author by id: (GET) /api/library/authors/booksOf/{authorId}


### Book

* Save a book: (POST) /api/library/books
* Find a book by id: (GET) /api/library/books/{bookId}
* List all books OR all books from a genre: (GET) /api/library/books
* Delete a book by id: (DELETE) /api/library/books/{bookId}
* Update a book by id: (PUT) /api/library/books/{bookId}
* List all EXEMPLARS of a book by id: (GET) /api/library/books/{bookId}/exemplars


### Exemplar

* Save an exemplar of the book: (POST) /api/library/exemplars/{bookId}
* Find an exemplar by id: (GET) /api/library/exemplars/{exemplarId}
* List all exemplars: (GET) /api/library/exemplars
* Delete an exemplar by id: (DELETE) /api/library/exemplars/{exemplarId}
* Update an exemplar by id: (PUT) /api/library/exemplars/{exemplarId}


### User

* Save a user: (POST) /api/library/users
* Find a user by id: (GET) /api/library/users/{userId}
* List all users: (GET) /api/library/users
* Delete a user by id: (DELETE) /api/library/users/{userId}
* Update a user by id: (PUT) /api/library/users/{userId}
* List all borrowings of a user by id: (GET) /api/library/users/{userId}/borrowings


### Borrowing

* Save a borrowing of an exemplar to a user: (POST) /api/library/borrowings/{exemplarId}/{userId}
* Find a borrowing by id: (GET) /api/library/borrowings/{borrowingId}
* List all borrowings OR all for an exemplar or for a user: (GET) /api/library/borrowings
* Delete a borrowing by id: (DELETE) /api/library/borrowings/{borrowingId}
* Extend a borrowing by id: (PUT) /api/library/borrowings/extend/{borrowingId}
* Inactivate a borrowing - book is back: (PUT) /api/library/borrowings/bring_back/{borrowingId}


