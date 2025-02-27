package tdd.elise.tp.models.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public enum Format {
    PAPERBACK("Livre de poche"),
    HARDCOVER("Livre Grand format"),
    EBOOK("Livre numérique"),
    ROMAN("Roman"),
    BD("Bande dessiné");

    private final String name;

    Format(String name) { // Constructeur explicite
        this.name = name;
    }
}
