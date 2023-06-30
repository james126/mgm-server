package mgm.service;

import mgm.model.dto.Enquiry;
import mgm.model.entity.Contact;

import java.util.Optional;

public interface EnquiryService {

    Optional<Contact> insertEnquiry(Enquiry form);

    public void deleteById(Integer id);
}
