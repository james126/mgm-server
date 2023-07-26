package mgm.model.mapper;

import mgm.model.entity.Contact;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ContactMapper implements RowMapper<Contact> {

    @Override
    public Contact mapRow(ResultSet rs, int rowNum) throws SQLException {
        Contact c = new Contact();
        c.setId(rs.getInt("id"));
        c.setFirst_name(rs.getString("first_name"));
        c.setLast_name(rs.getString("last_name"));
        c.setEmail(rs.getString("email"));
        c.setPhone(rs.getString("phone"));
        c.setAddress_line1(rs.getString("address_line1"));
        c.setAddress_line2(rs.getString("address_line2"));
        c.setMessage(rs.getString("message"));
        c.setUpdate_datetime(rs.getDate("update_dateTime"));
        return c;
    }
}
