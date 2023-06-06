package main.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Enquiry {
    private String first_name;
    private String last_name;
    private String email;
    private String phone;
    private String address_line1;
    private String address_line2;
    private String message;
}
