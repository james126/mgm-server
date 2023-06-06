package main.service;

import main.model.Contact;
import main.model.Enquiry;
import main.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

@Service
@Transactional
public class EnquiryServiceImplementation implements EnquiryService {

    private final ContactRepository contactRepository;

    @Autowired
    public EnquiryServiceImplementation(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Override
    public void deleteById(Integer id) {
    }

    @Override
    public List<Contact> selectEnquiries() {
        return contactRepository.selectEnquiries();
    }

    @Override
    public Contact insertEnquiry(Enquiry form){
        Contact c = new Contact();
        c.setFirst_name(form.getFirst_name());
        c.setLast_name(form.getLast_name());
        c.setEmail(form.getEmail());
        c.setPhone(form.getPhone());
        c.setAddress_line1(form.getAddress_line1());
        c.setAddress_line2(form.getAddress_line1());
        c.setMessage(form.getMessage());
        c.setUpdate_datetime(new Date(1,1,1));
        return contactRepository.save(c);
    }

}
