package main.service;

import main.model.Contact;
import main.model.Enquiry;

import java.util.List;

public interface EnquiryService {

    Contact insertEnquiry(Enquiry form);

    List<Contact> selectEnquiries();

    void deleteById(Integer id);
}
