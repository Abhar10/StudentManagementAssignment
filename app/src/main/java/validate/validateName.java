package validate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class validateName checks if the input name is valid or not
 */
public class validateName {

    private static final Pattern usrNamePtrn = Pattern.compile("^[a-zA-Z\\s]*$");

    /**
     *
     * @param userName The name to be checked whether valid or not
     * @return boolean value whether true or false
     */
    public static boolean isvalidUserName(String userName) {

        Matcher mtch = usrNamePtrn.matcher(userName);
        if (mtch.matches()) {
            return true;
        }
        return false;
    }

    /**
     * Function to check whether the entered name is empty or consists of only white spaces
     * @param userName The username which is to be checked
     * @return a boolean whether name empty or not
     */
    public static boolean isEmptyName(String userName)
    {
        if(userName.trim().length() == 0)
        {
           return true;
        }
        else
        {
            return false;
        }
    }
}
