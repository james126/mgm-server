package mgm.repository;

import mgm.model.entity.Authorities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthoritiesRepository extends JpaRepository<Authorities, Integer> {

    @Query("SELECT a FROM Authorities a WHERE a.username = :username")
    Optional<Authorities> findAuthorityByUsername(String username);
}
