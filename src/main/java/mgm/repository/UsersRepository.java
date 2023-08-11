package mgm.repository;

import mgm.model.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {

    @Query("SELECT u FROM Users u WHERE u.username = :username")
    Optional<Users> findByUsername(String username);
}
