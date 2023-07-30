package mgm.service;

import mgm.model.entity.Contact;

import java.util.List;
import java.util.Optional;

public interface ContactService {

    void insertContact(Contact form);
    //Test ok
    //Test when form is null
    //Test when form non fields are null
    //tet when fields are incompatible types

    void deleteById(Integer id);
    //delete by id
    //delete by null
    //delete by string
    //delete by id doesnt exist

    Optional<Contact> findByMinId();
    //find by min id
    //find by min id when database is blank

    List<Contact> getAllContacts();

    Optional<Contact> getNextContactForm(Integer id);
    //find by id
    //find by null
    //find by string
    //find by id doesnt exist
}
