package mgm.repository;

import mgm.model.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LoginRepository extends JpaRepository<Users, Integer> {
    @Query("SELECT u.temporaryPassword FROM Users u WHERE u.username = :username" )
    boolean isTemporaryPassword(@Param("username") String username);
}
