package mgm.service;

import mgm.model.entity.Contact;
import mgm.model.dto.Enquiry;

import java.util.List;
import java.util.Optional;

public interface EnquiryService {

    Optional<Contact> insertEnquiry(Enquiry form);

    List<Contact> selectEnquiries();

    void deleteById(Integer id);
}
