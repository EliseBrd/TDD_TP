package tdd.elise.tp.manager;

import tdd.elise.tp.exceptions.BookNotFoundException;
import tdd.elise.tp.models.Book;
import tdd.elise.tp.service.BookDataService;

import java.util.List;

public class BookManager {

    private final BookDataService databaseService;

    private final BookDataService webService;

    public BookManager(BookDataService databaseService, BookDataService webService) {
        this.databaseService = databaseService;
        this.webService = webService;
    }
    public Book getBookData(String isbn) {
        Book book = databaseService.getBookByISBN(isbn);

        if (book == null || book.getTitle() == null || book.getAuthor() == null || book.getEditor() == null || book.getFormat() == null || book.getAvailable() == null) {
            Book completeBook = webService.getBookByISBN(isbn);

            if (completeBook == null) {
                throw new BookNotFoundException();
            }
            if (book == null) {
                databaseService.put(completeBook); // Ajoute le livre complet en base
            } else {
                book.setIsbn(completeBook.getIsbn());
                book.setTitle(completeBook.getTitle());
                book.setAuthor(completeBook.getAuthor());
                book.setEditor(completeBook.getEditor());
                book.setFormat(completeBook.getFormat());
                book.setAvailable(completeBook.getAvailable());
                databaseService.put(book); // Met à jour le livre en base
            }

            return completeBook;
        }

        return book;
    }

    // récupéré une liste de book avec un titre
    public List<Book> getBooksByTitle(String title) {
        return databaseService.findByTitle(title);
    }

    // récupéré une liste de book avec un auteur
    public List<Book> getBooksByAuthor(String author) {
        return databaseService.findByAuthor(author);
    }

    public Book updateBook(Book book, String title, String newTitle) {
        return null;
    }
}
