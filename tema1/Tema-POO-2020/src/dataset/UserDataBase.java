package dataset;

import java.util.HashMap;
import java.util.List;
import user.User;

/**
 * Information about all users and methods of processing their data, retrieved
 * from input database
 */
public final class UserDataBase {
    /**
     * Information about users, saved from input
     */
    private final List<User> usersData;

    public UserDataBase(final List<User> usersData) {
        this.usersData = usersData;
    }

    public List<User> getUsersData() {
        return usersData;
    }

    /**
     * Method that creates a HashMap of users'name according to the number of
     * ratings they gave.
     *
     * @return a HashMap ---> username | number of ratings
     */
    public HashMap<String, Double> getNumberOfRatingsMap() {

        HashMap<String, Double> usersMap = new HashMap<String, Double>();

        for (User user : usersData) {
            if (user.getRatingNr() != 0) {
                usersMap.put(user.getUsername(), Double.valueOf(user.getRatingNr()));
            }
        }

        return usersMap;
    }

}
