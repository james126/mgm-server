package mgm.service;

import mgm.model.entity.Contact;
import mgm.model.mapper.ContactMapper;
import mgm.repository.ContactRepository;
import mgm.utility.DateTime;
import mgm.utility.StringSanitiser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.NonNull;
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
    public void insertContact(@NonNull Contact form){
        form.setUpdate_datetime(dateTime.getDate());
        Contact clean = sanitiser.sanitise(form);
        contactRepository.saveAndFlush(clean);
    }

    @Override
    public void deleteById(@NonNull Integer id) {
        contactRepository.deleteById(id);
        contactRepository.flush();
    }

    @Override
    public Optional<Contact> findByMinId(){
        return contactRepository.findByMinId();
    }

    @Override
    public Optional<Contact> getNextContactForm(@NonNull Integer id) {
        Optional<Contact> result = contactRepository.viewNext(id);
        if (result.isPresent()) {
            return result;
        } else {
            return contactRepository.findByMinId();
        }
    }

    @Override
    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }
}
