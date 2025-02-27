package tdd.elise.tp.models.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public enum Civility {
    MR("Monsieur"),
    MRS("Madame"),
    MISS("Mademoiselle");

    private final String name;

    Civility(String name) { // ✅ Constructeur explicite
        this.name = name;
    }
}
