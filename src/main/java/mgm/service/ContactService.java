package mgm.service;

import mgm.model.entity.Contact;

import java.util.List;
import java.util.Optional;

public interface ContactService {

    void insertContact(Contact form);

    void deleteById(Integer id);

    Optional<Contact> findById(Integer id);

    Optional<Contact> findByMinId();

    List<Contact> getAllContacts();

    Optional<Contact> getNextContactForm(Integer id);
}
