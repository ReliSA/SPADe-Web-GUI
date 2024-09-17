package cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
//TODO should probably be replaced by some third party encryption lib in the future
public class Crypto {
    protected static final int SALT_LEN = 32;
    protected static final int PASS_LEN = 64;
    /**
     * Method to hash password
     * @param password  password from client
     * @return          hashed password for database
     */
    public static String hashString(String password) {
        //standard java security encryption module
        MessageDigest digest = null;
        try {
            //try to instance the class - throws an error if algorithm
            digest = MessageDigest.getInstance("SHA3-256");
        }
        catch(NoSuchAlgorithmException exception){
            exception.printStackTrace();
            return null;
        }
        byte [] tmp = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        //convert byte array into string
        return (new HexBinaryAdapter()).marshal(tmp);
    }

    /**
     * Hash string and add salt to the end
     * prevention against rainbow table attacks
     * @param password String to be hashed
     * @return String salted hash of the input
     */
    public static String hashStringSalt(String password){
        String hash = hashString(password);
        Random r = new Random();
        StringBuffer sb = new StringBuffer();
        while(sb.length() < SALT_LEN){
            sb.append(Integer.toHexString(r.nextInt()));
        }
        String salt = sb.substring(0,SALT_LEN);
        hash += salt;
        return hash;
    }



    /**
     * Method compares user password with stored hash
     * @param password String user provided password
     * @param hash String hash saved in database
     * @return true if hashes are the same
     */
    public static boolean compareHashes(String password, String hash) {
        final String passwordHash = hashString(password);
        return hash.equals(passwordHash);
    }

    /**
     * Method compares user password with stored hash with salt
     * @param password String user provided password
     * @param hash String hash saved in database
     * @return true if hashes are the same
     */
    public static boolean compareHashesSalt(String password, String hash){
        //toss the salt aside
        String strippedHash = hash.substring(0,PASS_LEN);
        return compareHashes(password,strippedHash);
    }


}
