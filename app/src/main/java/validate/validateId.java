package validate;

public class validateId {
    /**
     * Function to check whether the entered Roll number is empty or consists of only white spaces
     * @param userId The username which is to be checked
     * @return a boolean whether name empty or not
     */
    public static boolean isEmptyId(String userId)
    {
        if(userId.trim().isEmpty())
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
