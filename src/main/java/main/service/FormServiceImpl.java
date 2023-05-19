package main.service;

import main.entity.Form;
import main.exception.DataPersistenceException;
import main.repository.FormRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FormServiceImpl implements FormService{

    private final FormRepository formRepository;

    public FormServiceImpl(FormRepository formRepository) {
        this.formRepository = formRepository;
    }

    @Override
    public Optional<Form> insertForm(Form form) {
        try{
            return Optional.of(formRepository.saveAndFlush(form));
        } catch (Exception e){
            throw new DataPersistenceException();
        }
    }

    @Override
    public List<Form> selectForms() {
        return formRepository.findAll();
    }
}
