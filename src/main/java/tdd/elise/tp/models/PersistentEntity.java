package tdd.elise.tp.models;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public class PersistentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
}

