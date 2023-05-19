package main.repository.mappers;

import main.entity.Form;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FormMapper implements RowMapper<Form> {

    @Override
    public Form mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Form form = new Form();
        form.setFirstName(resultSet.getString("first_name"));
        form.setLastName(resultSet.getString("last_name"));
        return form;
    }
}
