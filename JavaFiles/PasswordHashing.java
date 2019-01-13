import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
* Class to return MD5 hashed passwords for security purposes.
*
* @author  Kashif Ahmed - 18061036
* @version 1.0
* @since   November 2018 
*/
public class PasswordHashing {
	
	/**
	* Generates salted MD5 hashed password.
	*
	* @param passwordToHash password that needs to be hashed.
	* @exception NoSuchAlgorithmException in-case missing algorithm.
	*/
    public static String getSecurePassword(String passwordToHash)
    {
        String generatedPassword = null;
        try {
        	
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
            md.update("msc-KASHIF-AHMED-projectEEEEE495A7981C40622D49F9".getBytes());
            //Get the hash's bytes
            byte[] bytes = md.digest(passwordToHash.getBytes());
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }
}
