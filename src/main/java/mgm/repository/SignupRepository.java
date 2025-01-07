package mgm.repository;

import mgm.model.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SignupRepository extends JpaRepository<Users, Integer> {
    @Query("SELECT " +
            "CASE WHEN EXISTS" +
                "(" +
                    "SELECT u FROM Users u WHERE u.username = :username" +
                ")" +
                "THEN 'TRUE' ELSE 'FALSE'" +
            "END")
    boolean usernameTaken(@Param("username") String username);

    @Query("SELECT " +
            "CASE WHEN EXISTS" +
            "(" +
            "SELECT u FROM Users u WHERE u.email = :email" +
            ")" +
            "THEN 'TRUE' ELSE 'FALSE'" +
            "END")
    boolean emailTaken(@Param("email") String email);
}
