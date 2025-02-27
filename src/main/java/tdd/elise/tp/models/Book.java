package tdd.elise.tp.models;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tdd.elise.tp.models.enums.Format;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Book extends PersistentEntity {
    private String isbn;
    private String title;
    private String author;
    private String editor;

    @Enumerated(EnumType.STRING)
    private Format format;
    private Boolean available;
}
