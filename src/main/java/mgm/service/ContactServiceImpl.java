package mgm.service;

import mgm.model.entity.Contact;
import mgm.model.mapper.ContactMapper;
import mgm.repository.ContactRepository;
import mgm.service.sanitise.StringSanitiser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@EnableConfigurationProperties(DateTime.class)
public class ContactServiceImpl extends ContactMapper implements ContactService {
    private final ContactRepository contactRepository;
    private final DateTime dateTime;
    private final StringSanitiser sanitiser;

    @Autowired
    public ContactServiceImpl(ContactRepository contactRepository, DateTime zone, StringSanitiser sanitiser) {
        this.contactRepository = contactRepository;
        this.dateTime = zone;
        this.sanitiser = sanitiser;
    }

    @Override
    public void insertContact(Contact form){
        form.setUpdate_datetime(dateTime.getDate());
        Contact clean = sanitiser.sanitise(form);
        contactRepository.saveAndFlush(clean);
    }

    @Override
    public void deleteById(Integer id) {
        contactRepository.deleteById(id);
        contactRepository.flush();
    }

    @Override
    public Optional<Contact> findById(Integer id){
        return contactRepository.findById(id);
    }

    @Override
    public Optional<Contact> findByMinId(){
        return contactRepository.findByMinId();
    }

    @Override
    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
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
