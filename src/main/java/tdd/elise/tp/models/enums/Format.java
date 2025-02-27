package tdd.elise.tp.models.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Format {
    PAPERBACK("Livre de poche"),
    HARDCOVER("Livre Grand format"),
    EBOOK("Livre numérique"),
    ROMAN("Roman"),
    BD("Bande dessiné");

    private final String name;
}
