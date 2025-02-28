package tdd.elise.tp.service;

import tdd.elise.tp.models.Book;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookDataService {

    public Book getBookByISBN(String isbn) {
        return null;
    }

    public List<Book> findByTitle(String title) {
        return null;
    }

    public List<Book> findByAuthor(String author) {
        return null;
    }

    public Book put(Book book) {
        return null;
    }

    public Book putAuthor(String author) {
        return null;
    }

    public Book updateBook(Book book, String title, String newTitle) {
        if (book != null && book.getTitle().equals(title)) {
            // Logique de mise à jour ici, par exemple changer le titre
            book.setTitle(newTitle);
            return book; // Retourner le livre mis à jour
        }
        return null;
    }
}
