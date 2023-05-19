package main.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Form implements Persistable<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String firstName;
    private String lastName;

    @Override
    public Integer getId() {
        return id;
    }

    //prevent Spring Data doing a select-before-insert - this particular entity is never updated
    @Override
    public boolean isNew() {
        return true;
    }
}
