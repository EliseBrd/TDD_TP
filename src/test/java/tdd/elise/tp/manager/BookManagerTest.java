package tdd.elise.tp.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import tdd.elise.tp.exceptions.InvalidBookException;
import tdd.elise.tp.exceptions.InvalidIsbnCharacterException;
import tdd.elise.tp.manager.BookManager;
import tdd.elise.tp.models.Book;
import tdd.elise.tp.models.enums.Format;
import tdd.elise.tp.service.BookDataService;
import tdd.elise.tp.service.IsbnValidator;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookManagerTest {
    @Mock
    private BookDataService fakeDatabaseService;

    @Mock
    private BookDataService fakeWebService;

    private BookManager bookManager;

    private final String ISBN = "2253009687";
    private final String TITLE = "Notre Dame de Paris";
    private final String AUTHOR = "Victor Hugo";
    private final String EDITOR = "Guillaume";

    @BeforeEach
    /*public void init() {
        fakeDatabaseService = mock(BookDataService.class);
        fakeWebService = mock(BookDataService.class);
    }*/
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bookManager = new BookManager(fakeDatabaseService, fakeWebService);
    }
    /* -------------------------------------------------------
     *                      GIVEN
     * -------------------------------------------------------*/
    // Cas 1 : Si le livre est en base de données, il est retourné directement, sans appel au web service.
    @Test
    public void givenBookIsInDB_getBookDataFromDB() {
        // Given : le livre est présent en base
        Book expectedBook = new Book(ISBN, TITLE, AUTHOR, EDITOR, Format.PAPERBACK, true);
        when(fakeDatabaseService.getBookByISBN(ISBN)).thenReturn(expectedBook);

        // When : on cherche le livre par ISBN
        Book book = bookManager.getBookData(ISBN);

        // Then : on doit obtenir le livre depuis la DB sans appeler le web service
        verifyNoInteractions(fakeWebService); // Vérifie qu'on ne fait pas appel au web service
        assertEquals(expectedBook, book); // Vérifie que le livre retourné est bien celui en base
    }

    // Cas 2 : Si le livre n'est pas en base mais disponible sur le web service, il est retourné.
    @Test
    public void givenBookIsNotInDB_getBookDataFromWebService() {
        // Given : le livre n'est pas en base mais existe sur le web service
        Book expectedBook = new Book(ISBN, TITLE, AUTHOR, EDITOR, Format.HARDCOVER, true);
        when(fakeDatabaseService.getBookByISBN(ISBN)).thenReturn(null); // Pas en DB
        when(fakeWebService.getBookByISBN(ISBN)).thenReturn(expectedBook); // Trouvé sur le web

        // When : on cherche le livre par ISBN
        Book book = bookManager.getBookData(ISBN);

        // Then : le livre retourné doit être celui du web service
        assertEquals(expectedBook, book);
    }

    // Cas 3 : Si le livre n'est pas en base mais trouvé sur le web service, il doit être stocké en DB.
    @Test
    public void givenBookIsNotInDB_bookFromWebServiceIsStoredInDB() {
        // Given : le livre n'est pas en base mais trouvé sur le web service
        Book expectedBook = new Book(ISBN, TITLE, AUTHOR, EDITOR, Format.EBOOK, true);
        when(fakeDatabaseService.getBookByISBN(ISBN)).thenReturn(null);
        when(fakeWebService.getBookByISBN(ISBN)).thenReturn(expectedBook);

        // When : on cherche le livre par ISBN
        bookManager.getBookData(ISBN);

        // Then : on vérifie qu'on stocke bien le livre trouvé en base de données
        verify(fakeDatabaseService).put(expectedBook);
    }

    // Cas 4 : Si le livre n'est ni en base ni sur le web service, une exception doit être levée.
    @Test
    public void givenBookIsNotInDBAndWebService_getBookDataThrowsException() {
        // Given : le livre n'est ni en base ni sur le web service
        when(fakeDatabaseService.getBookByISBN(ISBN)).thenReturn(null);
        when(fakeWebService.getBookByISBN(ISBN)).thenReturn(null);

        // When & Then : une exception doit être levée car aucune information n'est trouvée
        assertThrows(InvalidBookException.class, () -> bookManager.getBookData(ISBN));
    }

    // Cas 5 : Si des informations manquent en base, elles doivent être complétées par le web service.
    @Test
    public void givenBookWithMissingData_fetchFromWebService() {
        // Given : un livre est en base mais il manque des informations (par exemple, l'auteur et l'éditeur)
        Book incompleteBook = new Book(ISBN, TITLE, null, null, Format.PAPERBACK, true);
        Book completeBook = new Book(ISBN, TITLE, "Victor Hugo", "Gallimard", Format.PAPERBACK, true);

        when(fakeDatabaseService.getBookByISBN(ISBN)).thenReturn(incompleteBook); // Livre incomplet en base
        when(fakeWebService.getBookByISBN(ISBN)).thenReturn(completeBook); // Livre complet trouvé sur le web service

        // When : on cherche le livre par ISBN
        Book book = bookManager.getBookData(ISBN);

        // Then : les informations manquantes doivent être complétées et mises à jour en base
        assertThat(book)
                .usingRecursiveComparison()
                .isEqualTo(completeBook);

        verify(fakeDatabaseService).put(argThat(updatedBook ->
                updatedBook.getIsbn().equals(completeBook.getIsbn()) &&
                        updatedBook.getTitle().equals(completeBook.getTitle()) &&
                        updatedBook.getAuthor().equals(completeBook.getAuthor()) &&
                        updatedBook.getEditor().equals(completeBook.getEditor()) &&
                        updatedBook.getFormat() == completeBook.getFormat() &&
                        updatedBook.getAvailable().equals(completeBook.getAvailable())
        ));

    }

    // Cas 6 : Rechercher un livre par ISBN (si présent en base)
    @Test
    public void givenBookExistsInDB_whenSearchingByISBN_thenReturnBook() {
        // Given : un livre est présent en base avec un ISBN donné
        Book expectedBook = new Book(ISBN, TITLE, AUTHOR, EDITOR, Format.PAPERBACK, true);
        when(fakeDatabaseService.getBookByISBN(ISBN)).thenReturn(expectedBook);

        // When : on cherche le livre par ISBN
        Book book = bookManager.getBookData(ISBN);

        // Then : le livre retourné doit être celui attendu
        assertEquals(expectedBook, book);
    }

    // Cas 7 : Rechercher un livre par titre (si présent en base)
    @Test
    public void givenBooksInDB_whenSearchingByTitle_thenReturnMatchingBooks() {
        // Given : plusieurs livres en base, dont un qui correspond au titre recherché
        List<Book> expectedBooks = List.of(
                new Book("123", "Le Comte de Monte-Cristo", "Alexandre Dumas", "Gallimard", Format.HARDCOVER, true),
                new Book("456", "Le Comte de Monte-Cristo", "Alexandre Dumas", "Folio", Format.PAPERBACK, true)
        );
        when(fakeDatabaseService.findByTitle("Le Comte de Monte-Cristo")).thenReturn(expectedBooks);

        // When : on cherche les livres par titre
        List<Book> books = bookManager.getBooksByTitle("Le Comte de Monte-Cristo");

        // Then : on doit retrouver tous les livres correspondants
        assertEquals(expectedBooks, books);
    }

    // Cas 8 : Rechercher un livre par auteur (si présent en base)
    @Test
    public void givenBooksInDB_whenSearchingByAuthor_thenReturnMatchingBooks() {
        // Given : plusieurs livres en base écrits par le même auteur
        List<Book> expectedBooks = List.of(
                new Book("123", "Les Misérables", "Victor Hugo", "Gallimard", Format.EBOOK, true),
                new Book("789", "Notre-Dame de Paris", "Victor Hugo", "Folio", Format.PAPERBACK, true)
        );
        when(fakeDatabaseService.findByAuthor("Victor Hugo")).thenReturn(expectedBooks);

        // When : on cherche les livres par auteur
        List<Book> books = bookManager.getBooksByAuthor("Victor Hugo");

        // Then : on doit retrouver tous les livres de cet auteur
        assertEquals(expectedBooks, books);
    }
}