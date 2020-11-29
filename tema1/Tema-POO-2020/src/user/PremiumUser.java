package user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import action.MyHashMap;
import dataset.UserDataBase;
import dataset.VideoDataBase;
import fileio.ActionInputData;
import show.Show;

/**
 * Class that contains particularities and methods of data processing of premium
 * users
 */
public final class PremiumUser extends User {

    public PremiumUser(final String username, final String subscriptionType,
            final Map<String, Integer> history, final ArrayList<String> favoriteMovies) {
        super(username, subscriptionType, history, favoriteMovies);
    }

    /**
     * This method searches for the first unseen video in the most popular genre
     * category
     *
     * @param userData  information about users'database
     * @param videoData information about videos'database
     * @param command   current action
     * @return the final message to be displayed
     */
    public String getPopularVideo(final UserDataBase userData, final VideoDataBase videoData,
            final ActionInputData command) {
        String message = "PopularRecommendation result: ";

        HashMap<String, Double> genreMap = new HashMap<String, Double>();
        MyHashMap map = new MyHashMap(genreMap);

        // Create a HashMap of genres according to their popularity
        for (User user : userData.getUsersData()) {
            for (Map.Entry<String, Integer> entry : user.getHistory().entrySet()) {

                // Get the number of views
                Double views = Double.valueOf(entry.getValue());
                for (Show video : videoData.getVideosData()) {
                    if (video.getTitle().equals(entry.getKey())) {
                        for (String genre : video.getGenres()) {

                            // Check if the key already exists
                            Double success = map.isInMap(genre);
                            if (success != 0) {

                                // Update the value of the key
                                Double newValue = success + views;
                                genreMap.replace(genre, newValue);
                            } else {

                                // Insert new key and value
                                genreMap.put(genre, views);
                            }
                        }
                    }
                }
            }
        }

        // Sort the map
        HashMap<String, Double> sortedMap = map.sortMapByValue(command);

        // Iterate through the genre map, searching for the first video not viewed by
        // the current user
        for (Map.Entry<String, Double> entry : sortedMap.entrySet()) {

            // Extract the most popular genre
            String mostPopularGenre = entry.getKey();
            for (Show video : videoData.getVideosData()) {
                for (String genre : video.getGenres()) {
                    if (genre.equals(mostPopularGenre)) {

                        // Check if the video is seen by the user
                        if (!isInHistory(video.getTitle())) {

                            // Video found!
                            message += video.getTitle();
                            return message;
                        }

                    }
                }
            }
        }

        message = "PopularRecommendation cannot be applied!";
        return message;
    }

    /**
     * This method searches for the first movie most "loved" (favorite) by users,
     * checking if it has not already been watched by the current user
     *
     * @param userData  information about users'database
     * @param videoData information about videos'database
     * @param command   current action
     * @return the final message to be displayed
     */
    public String getFavoriteVideo(final UserDataBase userData, final VideoDataBase videoData,
            final ActionInputData command) {
        String message = "FavoriteRecommendation result: ";

        // Create a HashMap --> videos | number of "being favorite"
        HashMap<String, Double> favoriteMap = videoData.getFavoriteMap(command, userData);
        MyHashMap map = new MyHashMap(favoriteMap, videoData);
        HashMap<String, Double> sortedMap = map.sortMapByValue(command);

        // Iterate through favorite map
        Iterator<String> iterator = sortedMap.keySet().iterator();
        String key = null;

        // Check if the video is seen by the user
        while (iterator.hasNext()) {

            key = iterator.next();
            if (!isInHistory(key)) {

                // Video found!
                message += key;
                return message;
            }
        }
        message = "FavoriteRecommendation cannot be applied!";
        return message;
    }

    /**
     * This method searches for all videos not viewed by the current user, which
     * belong to a genre mentioned in the given action. All videos will be stored in
     * an array which will represent the final result
     *
     * @param userData  information about users'database
     * @param videoData information about videos'database
     * @param command   current action
     * @return the final message to be displayed
     */
    public String getSearchVideo(final UserDataBase userData, final VideoDataBase videoData,
            final ActionInputData command) {
        String message = "SearchRecommendation result: ";
        ArrayList<String> searchVideo = new ArrayList<String>();

        // Create a HashMap --> videos | rating
        HashMap<String, Double> ratingMap = videoData.getRatingMap(command);
        MyHashMap map = new MyHashMap(ratingMap, videoData);
        HashMap<String, Double> sortedMap = map.sortMapByValue(command);

        // Iterate through rating map
        Iterator<String> iterator = sortedMap.keySet().iterator();
        String key = null;
        while (iterator.hasNext()) {
            key = iterator.next();
            for (Show video : videoData.getVideosData()) {
                if (video.getTitle().equals(key)) {

                    // Check if the movie belongs to the genre mentioned in the action filter
                    for (String genre : video.getGenres()) {
                        if (genre.equals(command.getGenre())) {

                            // Check if the video is seen by the user
                            if (!isInHistory(key)) {
                                searchVideo.add(key);
                            }
                        }
                    }
                }
            }
        }

        // Create final message
        message += searchVideo.toString();

        if (searchVideo.size() == 0) {
            message = "SearchRecommendation cannot be applied!";
        }

        return message;
    }

}
