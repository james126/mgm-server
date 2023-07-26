package mgm.service;

import mgm.model.entity.Contact;
import mgm.model.mapper.ContactMapper;
import mgm.repository.ContactRepository;
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

    @Autowired
    public ContactServiceImpl(ContactRepository contactRepository, DateTime zone) {
        this.contactRepository = contactRepository;
        this.dateTime = zone;
    }

    @Override
    public void deleteById(Integer id) {
        contactRepository.deleteById(id);
        contactRepository.flush();
    }

    public Optional<Contact> findById(Integer id){
        return contactRepository.findById(id);
    }

    @Override
    public Optional<Contact> findByMinId(){
        return contactRepository.findByMinId();
    }

    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }

    public void insertContact(Contact form){
        form.setUpdate_datetime(dateTime.getDate());
        contactRepository.saveAndFlush(form);
    }


}
