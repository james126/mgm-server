package mgm.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Persistable;

import java.util.Objects;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Contact implements Persistable<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String first_name;
//    private String last_name;
//    private String email;
//    private String phone;
//    private String address_line1;
//    private String address_line2;
//    private String message;
//    private Date update_datetime;

    @Override
    public Integer getId() {
        return id;
    }

    //prevent Spring Data doing a select-before-insert - this particular entity is never updated
    @Override
    public boolean isNew() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        Contact contact = (Contact) o;
        return getId() != null && Objects.equals(getId(), contact.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
