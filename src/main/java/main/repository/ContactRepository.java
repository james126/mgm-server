package main.repository;

import main.model.Contact;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends CrudRepository<Contact, Integer> {

    @Modifying
    @Query("DELETE FROM Contact c WHERE c.id = :id")
    void deleteById(@Param("id") Integer id);

    @Query("SELECT c FROM Contact c")
    List<Contact> selectEnquiries();
}
