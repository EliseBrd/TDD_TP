package tdd.elise.tp.service;

import tdd.elise.tp.exceptions.InvalidIsbnCharacterException;
import tdd.elise.tp.exceptions.InvalidIsbnLengthException;

public class IsbnValidator {

    boolean validateIsbn(String isbn) {
        if (isbn.length() != 10)
            throw new InvalidIsbnLengthException();

        int total = 0;
        for (int i = 0; i < 10; i++) {
            if (!Character.isDigit(isbn.charAt(i))) {
                if (i == 9 && isbn.charAt(9) == 'X') {
                    total += 10;
                    break;
                } else {
                    throw new InvalidIsbnCharacterException();
                }
            }
            total += Character.getNumericValue(isbn.charAt(i)) * (10 - i);
        }
        return total % 11 == 0;
    }

}
