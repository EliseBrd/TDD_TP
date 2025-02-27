package tdd.elise.tp.models;

import jakarta.persistence.Entity;
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
    private Author author;
    private Editor editor;
    private Format format;
    private Boolean available;
}
