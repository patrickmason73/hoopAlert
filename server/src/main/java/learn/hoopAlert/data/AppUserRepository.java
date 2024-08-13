package learn.hoopAlert.data;


import learn.hoopAlert.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE AppUser u SET u.passwordHash = :password WHERE u.username = :username")
    void updatePasswordByUsername(@Param("password") String password, @Param("username") String username);

    AppUser findByUsername(String username);

}