package mgm.service;

import mgm.model.entity.Contact;

import java.util.Optional;

public interface ContactProcessor {
    Optional<Contact> getNextContactForm(Integer id);
}
