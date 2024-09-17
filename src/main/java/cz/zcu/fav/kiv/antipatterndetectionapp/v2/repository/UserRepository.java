package cz.zcu.fav.kiv.antipatterndetectionapp.v2.repository;

import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Interface as database object
 * @version 1.0
 * @author Vaclav Hrabik, Jiri Trefil
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findUserByEmail(String email);
    User findByName(String name);
}
