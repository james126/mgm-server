package mgm.repository;

import mgm.model.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {

    @Query("SELECT u FROM Users u WHERE u.username = :username")
    Optional<Users> findByUsername(String username);

    @Query("SELECT " +
            "CASE WHEN EXISTS" +
            "(" +
            "SELECT u FROM Users u WHERE u.username = :username" +
            ")" +
            "THEN 'TRUE' ELSE 'FALSE'" +
            "END")
    boolean existsByUsername(String username);

    @Query("SELECT u FROM Users u WHERE u.email = :email")
    Optional<Users> findByEmail(String email);

    @Query("SELECT " +
            "CASE WHEN EXISTS" +
            "(" +
            "SELECT u FROM Users u WHERE u.email = :email" +
            ")" +
            "THEN 'TRUE' ELSE 'FALSE'" +
            "END")
    boolean emailExists(@Param("email") String email);
}
