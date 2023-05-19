package main.service;

import main.entity.Form;

import java.util.List;
import java.util.Optional;

public interface FormService {

    //Save operation
    Optional<Form> insertForm(Form form);

    //Read operation
    List<Form> selectForms();
}
