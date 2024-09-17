package cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.user;

import cz.zcu.fav.kiv.antipatterndetectionapp.v2.dials.UserModelStatusCodes;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.User;

/**
 * Interface implemented by service(s) which are responsible for user management (registration, login, logout)
 * @version 1.0
 * @author Vaclav Hrabik, Jiri Trefil
 */
public interface UserService {
    /**
     * Method attempts to register a user and returns status code indicating registration result
     * @param user serialized JSON object representing user
     * @return Integer - status code of the operation, ie 1 - successful, 0 - name taken, ...
     */
    public UserModelStatusCodes registerUser(User user);
    /**
     * Method attempts to log in a user and returns status code indicating login result
     * @param user serialized JSON object representing user
     * @return Integer - status code of the operation, ie 1 - successful, 0 - failed, ....
     */
    public UserModelStatusCodes verifyUser(User user);

    public User getUserByName(String name);

}
