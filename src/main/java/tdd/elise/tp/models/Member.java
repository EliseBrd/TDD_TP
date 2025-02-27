package tdd.elise.tp.models;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tdd.elise.tp.models.enums.Civility;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Member extends PersistentEntity {
    private String lastName;
    private String firstName;
    private Date birthDate;
    private Civility civility;
}
