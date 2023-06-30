package mgm.repository;

import mgm.model.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer> {

    @Modifying
    @Query("DELETE FROM Contact c WHERE c.id = :id")
    void deleteById(@Param("id") Integer id);
}
