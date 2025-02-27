package tdd.elise.tp.models.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Civility {
    MR("Monsieur"),
    MRS("Madame"),
    MISS("Mademoiselle");

    private final String name;
}
