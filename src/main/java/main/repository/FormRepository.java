package main.repository;

import main.entity.Form;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FormRepository extends JpaRepository<Form, Integer> {

    @Modifying
    @Query("DELETE FROM Form a WHERE a.id = :id")
    void deleteById(@Param("id") Integer id);

//
//    public void persistForm(@RequestBody Form form){
//        String sql = "INSERT INTO form (first_name, last_name) VALUES (?, ?)";
//        jdbc.update(sql, form.getFirstName(), form.getLastName());
//    }
//
//    public List<Form> getAllForms(){
//        String sql = "SELECT * FROM form";
//
//        RowMapper<Form> formRowMapper = (r, i) -> {
//            Form form = new Form();
//            form.setId(r.getInt("id"));
//            form.setFirstName(r.getString("first_name"));
//            form.setLastName(r.getString("last_name"));
//            return form;
//        };
//
//        return jdbc.query(sql, formRowMapper);
//    }
}
