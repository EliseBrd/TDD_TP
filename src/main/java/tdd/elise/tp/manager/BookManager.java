package tdd.elise.tp.manager;

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
    // récupéré un book avec son isbn
    public Book getBookData(String isbn) {
        return null;
    }

    // récupéré une liste de book avec un titre
    public List<Book> getBooksByTitle(String title) {
        return null;
    }

    // récupéré une liste de book avec un auteur
    public List<Book> getBooksByAuthor(String author) {
        return null;
    }
}
