package main.service;

import main.model.Contact;
import main.model.Enquiry;

import java.util.List;
import java.util.Optional;

public interface EnquiryService {

    Optional<Contact> insertEnquiry(Enquiry form);

    List<Contact> selectEnquiries();

    void deleteById(Integer id);
}
