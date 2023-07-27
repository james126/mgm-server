package mgm.service;

import mgm.model.entity.Contact;
import mgm.model.mapper.ContactMapper;
import mgm.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class ContactProcessorImpl extends ContactMapper implements ContactProcessor{
    private final ContactRepository contactRepository;

    @Autowired
    public ContactProcessorImpl(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Override public Optional<Contact> getNextContactForm(Integer id) {
        Optional<Contact> result = contactRepository.findById(id + 1);
        if (result.isPresent()) {
            return result;
        }

        result = contactRepository.findByMinId();
        return result;
    }

}
