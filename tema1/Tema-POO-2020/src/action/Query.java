package action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import dataset.ActorDataBase;
import dataset.UserDataBase;
import dataset.VideoDataBase;
import fileio.ActionInputData;

/**
 * Class that deals with the processing of "query" type actions
 */
public final class Query {
    /**
     * Information from input (database) on which will be performed different actions
     */
    private ActorDataBase actorData;
    private UserDataBase userData;
    private VideoDataBase videoData;

    /**
     * Current action
     */
    private ActionInputData command;
    /**
     * Message to be created and printed
     */
    private String message;

    public Query(final ActorDataBase actorData, final VideoDataBase videoData,
            final ActionInputData command, final UserDataBase userData) {
        this.userData = userData;
        this.actorData = actorData;
        this.videoData = videoData;
        this.command = command;
    }

    /**
     * Method that checks the object on which will be performed the action, checking
     * at the same time the action criteria. When the desired filters are met, a
     * specific hashtable will be created. After creating it, using sortByValue
     * method the HashTable will be sorted according to the action filters and
     * criteria.
     *
     * @return the message to be displayed
     */
    public String getQueryMessage() {

        MyHashMap map = null;

        switch (command.getObjectType()) {

        case "actors":
            switch (command.getCriteria()) {

            case "average":
                HashMap<String, Double> averageMap = actorData.getAverageMap(videoData);
                map = new MyHashMap(averageMap);
                break;
            case "awards":
                HashMap<String, Double> awardsMap = actorData.getAwardsMap(command);
                map = new MyHashMap(awardsMap);
                break;
            case "filter_description":
                message = actorData.getFilterDescription(command);
                return message;
            default:
                message = "Invalid command !";
            }
            break;

        case "movies":
        case "shows":

            switch (command.getCriteria()) {

            case "ratings":
                HashMap<String, Double> ratingMap = videoData.getRatingMap(command);

                map = new MyHashMap(ratingMap);
                break;
            case "favorite":
                HashMap<String, Double> favoriteMap = videoData.getFavoriteMap(command, userData);
                map = new MyHashMap(favoriteMap);
                break;
            case "longest":
                HashMap<String, Double> longestMap = videoData.getLongestMap(command);
                map = new MyHashMap(longestMap);
                break;
            case "most_viewed":
                HashMap<String, Double> viewsMap = videoData.getMostViewedMap(command, userData);
                map = new MyHashMap(viewsMap);
                break;

            default:
                message = "Invalid command !";

            }
            break;

        case "users":
            HashMap<String, Double> nrOfRatingsMap = userData.getNumberOfRatingsMap();
            map = new MyHashMap(nrOfRatingsMap);
            break;

        default:
            message = "Invalid command !";
        }

        // Sort the HashMap
        HashMap<String, Double> sortedMap = map.sortMapByValue(command);

        // Return the final message
        return getCommandMessage(sortedMap);
    }

    /**
     * This method creates the final message using an arrayList in which all the
     * selected keys will be stored.
     *
     * @param map contains the information to be extracted
     * @return the final message
     */
    public String getCommandMessage(final HashMap<String, Double> map) {

        ArrayList<String> nameList = new ArrayList<>();
        Set<String> keys = map.keySet();

        // Extract the number of objects that should be displayed
        int cnt = command.getNumber();

        // Create the arrayList
        for (String key : keys) {
            if (command.getNumber() != 0) {
                if (cnt != 0) {
                    nameList.add(key);
                    cnt--;
                }
            } else {
                nameList.add(key);
            }
        }

        String tempMessage = nameList.toString();
        message = "Query result: " + tempMessage;

        return message;
    }
}
