package tdd.elise.tp.service;

import tdd.elise.tp.exceptions.InvalidIsbnCharacterException;
import tdd.elise.tp.exceptions.InvalidIsbnLengthException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IsbnValidatorTest {

    @Test
    public void whenIsbnIsValid_shouldReturnTrue()  {
        // Given
        IsbnValidator validator = new IsbnValidator();
        // When
        boolean result = validator.validateIsbn("2253009687");
        // Should
        assertTrue(result);
    }

    @Test
    public void whenIsbnIsInvalid_shouldReturnFalse()  {
        // Given
        IsbnValidator validator = new IsbnValidator();
        // When
        boolean result = validator.validateIsbn("2253009684");
        // Should
        assertFalse(result);
    }

    @Test
    public void whenIsbnIs9Chars_shouldThrowInvalidLengthException() {
        // Given
        IsbnValidator validator = new IsbnValidator();
        // Should
        assertThrows(InvalidIsbnLengthException.class, () -> validator.validateIsbn("229039117"));
    }

    @Test
    public void whenIsbnIsValidAndEndsWithX_shouldReturnTrue()  {
        // Given
        IsbnValidator validator = new IsbnValidator();
        // When
        boolean result = validator.validateIsbn("140274577X");
        // Should
        assertTrue(result);
    }

    @Test
    public void whenIsbnHasInvalidChar_shouldThrowException()  {
        // Given
        IsbnValidator validator = new IsbnValidator();
        // When
        assertThrows(InvalidIsbnCharacterException.class, () -> validator.validateIsbn("14ze74577X"));
    }

}
