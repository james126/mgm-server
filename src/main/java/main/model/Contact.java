package main.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class Contact {
    private long id;
    private String first_name;
    private String last_name;
    private String email;
    private String phone;
    private String address_line1;
    private String address_line2;
    private String message;
    private Date update_datetime;
}
