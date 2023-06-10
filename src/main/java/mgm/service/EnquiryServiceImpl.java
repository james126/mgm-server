package mgm.service;

import mgm.model.dto.Enquiry;
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
public class EnquiryServiceImpl extends ContactMapper implements EnquiryService {
    private final ContactRepository contactRepository;
    private final DateTime dateTime;

    @Autowired
    public EnquiryServiceImpl(ContactRepository contactRepository, DateTime zone) {
        this.contactRepository = contactRepository;
        this.dateTime = zone;
    }

    @Override
    public void deleteById(Integer id) {
        contactRepository.deleteById(id);
        contactRepository.flush();
    }

    @Override
    public List<Contact> selectEnquiries() {
        return contactRepository.selectEnquiries();
    }

    @Override
    public Optional<Contact> insertEnquiry(Enquiry form){
        Contact c = mapEnquiry(form);
        c.setUpdate_datetime(dateTime.getDate());
        return Optional.of(contactRepository.saveAndFlush(c));
    }
}
