package mgm.repository;

import mgm.model.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer> {

    @Modifying
    @Query("DELETE FROM Contact c WHERE c.id = :id")
    void deleteById(@Param("id") Integer id);

    @Query("SELECT c FROM Contact c WHERE c.id = (SELECT MIN(c2.id) FROM Contact c2)")
    Optional<Contact> findByMinId();

    @Query("SELECT c FROM Contact c WHERE c.id = (SELECT MIN(c2.id) FROM Contact c2 where c2.id > :id)")
    Optional<Contact> viewNext(@Param("id") Integer id);
}
