package cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.user;

import cz.zcu.fav.kiv.antipatterndetectionapp.v2.dials.UserModelStatusCodes;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.User;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.repository.UserRepository;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils.Crypto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.regex.Pattern;

/**
 * Service provides functionality for user login/register/logout
 * @author Vaclav Hrabik, Jiri Trefil
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Method attempts to register a user
     * @param user serialized JSON object representing user
     * @return  Enum code UserModelStatusCodes.
     *      INVALID_USER_ARGUMENTS      if user send invalid name, email or password
     *      USER_EXISTS                 if user send name, which is already taken
     *      HASH_FAILED                 if hash function failed
     *      USER_CREATION_FAILED        if there is problem with database
     *      USER_CREATED                if user were created (happy-day)
     */
    @Override
    public UserModelStatusCodes registerUser(User user) {
        final String email = user.getEmail();
        final String name = user.getName();
        final String password = user.getPassword();

        if (!verifyUserParameters(name, email, password))
            return UserModelStatusCodes.INVALID_USER_ARGUMENTS;

        //if the username is taken, kill the request as well
        if (userRepository.findByName(user.getName()) != null) {
            return UserModelStatusCodes.USER_EXISTS;
        }

        String passwordHash = Crypto.hashString(password);
        //server side fault
        if (passwordHash == null) {
            return UserModelStatusCodes.HASH_FAILED;
        }
        //save the user
        User u = userRepository.save(new User(name, email, passwordHash));
        //database insert failed for some reason
        if(u == null) {
            return UserModelStatusCodes.USER_CREATION_FAILED;
        }

        //return okay status code, the user was created
        return UserModelStatusCodes.USER_CREATED;
    }

    /**
     * @param email String - user email provided by client
     * @param name String - username provided by client
     * @return true if @param User is valid
     */
    private boolean verifyUserParameters(String name, String email, String password) {
        //if client send somehow not whole register request.
        if (email == null || name == null || password == null) {
            return false;
        }
        //if client request violates constraints - kill the request. User will not be registered.
        if (email.length() > User.getEmailConstraint() || name.length() > User.getNameConstraint()) {
            return false;
        }
        //regex for email validation
        String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        final Pattern emailPattern = Pattern.compile(emailRegex);
        //return true if email is valid (pattern wise)
        return emailPattern.matcher(email).find();
    }



    /**
     * Method
     * @param user serialized JSON object representing user
     * @return  Enum flag UserModelStatusCodes.
     *      USER_LOGGED_IN     if user has right name and password
     *      USER_LOGIN_FAILED  if user has wrong name or password
     */

    @Override
    public UserModelStatusCodes verifyUser(User user) {
        final String name = user.getName();
        final String password = user.getPassword();
        if(name == null || password == null) {
            return UserModelStatusCodes.USER_LOGIN_FAILED;
        }

        User u = userRepository.findByName(user.getName());

        if (u == null) {
            return UserModelStatusCodes.USER_LOGIN_FAILED;
        }
        final boolean passwordMatches = Crypto.compareHashes(password, u.getPassword());

        return (!passwordMatches ? UserModelStatusCodes.USER_LOGIN_FAILED : UserModelStatusCodes.USER_LOGGED_IN);
    }



    @Override
    public User getUserByName(String name) {
        return this.userRepository.findByName(name);
    }


}
