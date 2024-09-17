package cz.zcu.fav.kiv.antipatterndetectionapp.v2.service;

import cz.zcu.fav.kiv.antipatterndetectionapp.v2.dials.UserModelStatusCodes;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.User;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.repository.UserRepository;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.user.UserService;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.user.UserServiceImpl;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils.Crypto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Tests for userServiceImplementation
 */
@SpringBootTest
public class UserServiceImpTest {

    /**
     * UserService
     */
    @Autowired
    private UserService userService;

    /**
     * Mocked userRepository
     */
    @MockBean
    private UserRepository userRepository;

    /**
     * Mocked User
     */
    private final User mockUser = new User("test", "test@tt.cz", "Ahoj99");

    /* <-------- REGISTRATION TESTING --------> */

    /**
     * To test happy-day scenario where everything is ok
     */
    @Test
    public void whenUserIsNotRegistered_thenRegisterHim() {
        when(userRepository.save(any())).thenReturn(mockUser);

        UserModelStatusCodes foundCode = userService.registerUser(mockUser);

        assertEquals(UserModelStatusCodes.USER_CREATED, foundCode);
    }

    /**
     * To test scenario where user want register which name, with more than 255 chars
     */
    @Test
    public void whenUserNameIsInvalid_thenReturnMSG() {
        User MUser = new User("asdasdasdasdasdasdasdafjsdjbfsdfbsdkncbsdcsdlkjdsbvjkhsbdvjhbxcvbjxcnvbxbjhdsbfjs" +
                "fsjdkdddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                "ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                "ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                "ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                "dddddddddddhfbsdjfhbsdjhbjckvxjhbvjhfbsjhbdjhsbvhbsckjhvbsdhfbsdjhvbjxhcbvhbjhvbxcv", "tt@aa.ct");
        UserModelStatusCodes foundCode = userService.registerUser(MUser);

        assertEquals(UserModelStatusCodes.INVALID_USER_ARGUMENTS, foundCode);
    }

    /**
     * To test scenario where user want register which email, with more than 255 chars
     */
    @Test
    public void whenUserEmailIsInvalid_thenReturnMSG() {
        User MUser = new User("Petr", "tasdddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                "dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                "dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                "dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                "ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddt@aa.ct");
        UserModelStatusCodes foundCode = userService.registerUser(MUser);

        assertEquals(UserModelStatusCodes.INVALID_USER_ARGUMENTS, foundCode);
    }

    /**
     * To test scenario where user want register which email, with email without "@"
     */
    @Test
    public void whenUserEmailIsInvalid_02_thenReturnMSG() {
        User MUser = new User("Petr", "testaa.ct");
        UserModelStatusCodes foundCode = userService.registerUser(MUser);

        assertEquals(UserModelStatusCodes.INVALID_USER_ARGUMENTS, foundCode);
    }

    /**
     * To test scenario where user want register which email, with email without "."
     */
    @Test
    public void whenUserEmailIsInvalid_03_thenReturnMSG() {
        User MUser = new User("Petr", "test@aact");
        UserModelStatusCodes foundCode = userService.registerUser(MUser);

        assertEquals(UserModelStatusCodes.INVALID_USER_ARGUMENTS, foundCode);
    }

    /**
     * To test scenario where user want register which email, with email without "@" and "."
     */
    @Test
    public void whenUserEmailIsInvalid_04_thenReturnMSG() {
        User MUser = new User("Petr", "testaact");
        UserModelStatusCodes foundCode = userService.registerUser(MUser);

        assertEquals(UserModelStatusCodes.INVALID_USER_ARGUMENTS, foundCode);
    }

    /**
     * To test scenario where user want register which name and email, with more than 255 chars
     */
    @Test
    public void whenUserNameAndEmailIsInvalid_thenReturnMSG() {
        User MUser = new User("asdasdasdasdasdasdasdafjsdjbfsdfbsdkncbsdcsdlkjdsbvjkhsbdvjhbxcvbjxcnvbxbjhdsbfjs" +
                "fsjdkdddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                "ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                "ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                "ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                "dddddddddddhfbsdjfhbsdjhbjckvxjhbvjhfbsjhbdjhsbvhbsckjhvbsdhfbsdjhvbjxhcbvhbjhvbxcv", "tasddddddd" +
                "ddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                "dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                "dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                "dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                "ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddt@aa.ct");
        UserModelStatusCodes foundCode = userService.registerUser(MUser);

        assertEquals(UserModelStatusCodes.INVALID_USER_ARGUMENTS, foundCode);
    }

    /**
     * scenario where username is empty
     */
    @Test
    public void whenUserNULLName_thenReturnMSG() {
        User MUser = new User(null, "tt@asd.cf", "Ahoj99");
        UserModelStatusCodes foundCode = userService.registerUser(MUser);

        assertEquals(UserModelStatusCodes.INVALID_USER_ARGUMENTS, foundCode);
    }
    /**
     * scenario where email is empty
     */
    @Test
    public void whenUserNULLEmail_thenReturnMSG() {
        User MUser = new User("tt", null, "Ahoj99");
        UserModelStatusCodes foundCode = userService.registerUser(MUser);

        assertEquals(UserModelStatusCodes.INVALID_USER_ARGUMENTS, foundCode);
    }

    /**
     * scenario where  password is empty
     */
    @Test
    public void whenUserNULLPassword_thenReturnMSG() {
        User MUser = new User("tt", "tt@asd.cf", null);
        UserModelStatusCodes foundCode = userService.registerUser(MUser);

        assertEquals(UserModelStatusCodes.INVALID_USER_ARGUMENTS, foundCode);
    }

    /**
     * scenario where username and email is empty
     */
    @Test
    public void whenUserNULLNameEmail_thenReturnMSG() {
        User MUser = new User(null, null, "Ahoj99");
        UserModelStatusCodes foundCode = userService.registerUser(MUser);

        assertEquals(UserModelStatusCodes.INVALID_USER_ARGUMENTS, foundCode);
    }

    /**
     * scenario where username and password is empty
     */
    @Test
    public void whenUserNULLNamePassword_thenReturnMSG() {
        User MUser = new User(null, "tt@asd.cf", null);
        UserModelStatusCodes foundCode = userService.registerUser(MUser);

        assertEquals(UserModelStatusCodes.INVALID_USER_ARGUMENTS, foundCode);
    }

    /**
     * scenario where email and password is empty
     */
    @Test
    public void whenUserNULLEmailPassword_thenReturnMSG() {
        User MUser = new User("tt", null, null);
        UserModelStatusCodes foundCode = userService.registerUser(MUser);

        assertEquals(UserModelStatusCodes.INVALID_USER_ARGUMENTS, foundCode);
    }

    /**
     * scenario where username, password, and email is empty
     */
    @Test
    public void whenUserNULLNameEmailPassword_thenReturnMSG() {
        User MUser = new User(null, null, null);
        UserModelStatusCodes foundCode = userService.registerUser(MUser);

        assertEquals(UserModelStatusCodes.INVALID_USER_ARGUMENTS, foundCode);
    }

    /**
     * To test scenario where user want to register with name, that is already taken
     */
    @Test
    public void whenUserIsRegistered_thenSendMSG() {
        when(userRepository.findByName(any())).thenReturn(mockUser);

        UserModelStatusCodes foundCode = userService.registerUser(mockUser);

        assertEquals(UserModelStatusCodes.USER_EXISTS, foundCode);
    }

    /* <-------- USER VERIFICATION TESTING --------> */

    /**
     * for happy-day scenario user is registered
     */
    @Test
    public void whenUserIsRegistered_thenVerifyHim() {
        String name = mockUser.getName();
        String password = "C63293087805FC0E63564B1F389A85DE00A369B01F3B4B0D0F3A949F565D2765";
        String email = mockUser.getEmail();

        when(userRepository.findByName(any())).thenReturn(new User(name, email, password));

        UserModelStatusCodes foundCode = userService.verifyUser(mockUser);

        assertEquals(UserModelStatusCodes.USER_LOGGED_IN, foundCode);
    }

    /**
     * for users name is not registered
     */
    @Test
    public void whenUserIsNotRegistered_thenReturnMSG() {

        when(userRepository.findByName(any())).thenReturn(null);

        UserModelStatusCodes foundCode = userService.verifyUser(mockUser);

        assertEquals(UserModelStatusCodes.USER_LOGIN_FAILED, foundCode);
    }

    /**
     * for scenario when user has wrong password
     */
    @Test
    public void whenUserHasWrongPassword_thenSendERROR_MSG() {
        String name = mockUser.getName();
        String password = "test";
        String email = mockUser.getEmail();

        when(userRepository.findByName(any())).thenReturn(new User(name, email, password));

        UserModelStatusCodes foundCode = userService.verifyUser(mockUser);

        assertEquals(UserModelStatusCodes.USER_LOGIN_FAILED, foundCode);
    }





    /* <-------- HASH TESTING --------> */
    /**
     * for happy-day scenario user is registered
     */
    @Test
    public void Hash_test() {
        String hash = "c63293087805fc0e63564b1f389a85de00a369b01f3b4b0d0f3a949f565d2765";

        UserServiceImpl userService1 = new UserServiceImpl();

        String foundCode = Crypto.hashString(mockUser.getPassword());

        assertEquals(hash.toUpperCase(), foundCode);
    }

}
