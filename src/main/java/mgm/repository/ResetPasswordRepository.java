package mgm.repository;

import mgm.model.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ResetPasswordRepository extends JpaRepository<Users, Integer> {
    @Modifying
    @Query("UPDATE Users u " +
            "SET u.password = :password, u.temporaryPassword = true " +
            "WHERE u.email = :email")
    void submitTempPassword(@Param("password") String password, @Param("email") String email);

    @Modifying
    @Query("UPDATE Users u " +
            "SET u.password = :password, u.temporaryPassword = false " +
            "WHERE u.username = :username")
    void updatePassword(@Param("password") String password, @Param("username") String username);
}
