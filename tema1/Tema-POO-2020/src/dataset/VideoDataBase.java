package dataset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import action.MyHashMap;
import fileio.ActionInputData;
import show.Show;
import user.User;

public final class VideoDataBase {

    /**
     * Information about videos, just serials or just movies, all saved from input.
     */
    private final List<Show> videosData;
    private final List<Show> moviesData;
    private final List<Show> serialsData;

    public VideoDataBase(final List<Show> videosData, final List<Show> moviesData,
            final List<Show> serialsData) {
        this.videosData = videosData;
        this.moviesData = moviesData;
        this.serialsData = serialsData;
    }

    public List<Show> getMoviesData() {
        return moviesData;
    }

    public List<Show> getSerialsData() {
        return serialsData;
    }

    public List<Show> getVideosData() {
        return videosData;
    }

    /**
     * Creates a list only with videos whose information matches to the filters in
     * the current action.
     *
     * @param videos  videos to be filtered
     * @param command curent action
     * @return the filtered list of videos
     */
    public List<Show> applyFilterVideos(final List<Show> videos, final ActionInputData command) {

        List<Show> filteredVideos = new ArrayList<Show>();

        for (Show video : videos) {
            if ((command.getFilters().get(0).get(0) == null
                    || command.getFilters().get(0).contains("" + video.getYear()))
                    && (command.getFilters().get(1).get(0) == null
                            || video.getGenres().containsAll(command.getFilters().get(1)))) {

                // All the conditions are met
                filteredVideos.add(video);
            }
        }

        return filteredVideos;
    }

    /**
     * Method that sets the appropriate list of objects to be analyzed (all videos,
     * only movies/serials).
     *
     * @param videoData videos's database
     * @param command   current action
     * @return list of desired videos
     */
    public List<Show> setVideoType(final ActionInputData command) {
        List<Show> videos = new ArrayList<Show>();

        if (command.getActionType().equals("query")) {

            if (command.getObjectType().equals("movies")) {
                videos = moviesData;
            } else if (command.getObjectType().equals("shows")) {
                videos = serialsData;
            }

        } else {
            videos = videosData;
        }

        // Final list of objects
        return videos;
    }

    /**
     * Creates a HashMap of video titles based on the number of views the video has,
     * using users'history.
     *
     * @param command  current action
     * @param userData information from user's database
     * @return a HashMap ---> video title | number of views
     */
    public HashMap<String, Double> getMostViewedMap(final ActionInputData command,
            final UserDataBase userData) {
        HashMap<String, Double> viewsMap = new HashMap<String, Double>();
        MyHashMap map = new MyHashMap(viewsMap);

        List<Show> videos = setVideoType(command);
        videos = applyFilterVideos(videos, command);

        // Create the HashMap
        for (Show video : videos) {
            for (User user : userData.getUsersData()) {
                for (Map.Entry<String, Integer> entry : user.getHistory().entrySet()) {
                    if (entry.getKey().equals(video.getTitle())) {
                        Double views = Double.valueOf(entry.getValue());

                        // Check if the key already exists
                        Double success = map.isInMap(video.getTitle());
                        if (success != 0) {
                            Double newValue = success + views;

                            // Update the value of the existing key
                            viewsMap.replace(video.getTitle(), newValue);
                        } else {

                            // Add new key and value
                            viewsMap.put(video.getTitle(), views);
                        }
                    }
                }
            }
        }

        return viewsMap;
    }

    /**
     * Creates a HashMap of video titles based on the rating recieved from different
     * users.
     *
     * @param command current action
     * @return a HashMap ---> video title | rating
     */
    public HashMap<String, Double> getRatingMap(final ActionInputData command) {
        final HashMap<String, Double> ratingMap = new HashMap<String, Double>();

        List<Show> videos = setVideoType(command);

        // Create HashMap
        if (command.getActionType().equals("query")) {

            videos = applyFilterVideos(videos, command);
            for (Show video : videos) {
                Double filmRating = video.getRating();
                if (filmRating != 0) {
                    ratingMap.put(video.getTitle(), filmRating);
                }
            }

        } else if (command.getActionType().equals("recommendation")) {

            for (Show video : videos) {
                Double filmRating = video.getRating();
                ratingMap.put(video.getTitle(), filmRating);
            }

        }

        return ratingMap;
    }

    /**
     * Creates a HashMap of video titles according to the number of occurences in
     * users'favorite lists.
     *
     * @param command  current action
     * @param userData information from users database
     * @return a HashMap ---> video title | number of "being favorite"
     */
    public HashMap<String, Double> getFavoriteMap(final ActionInputData command,
            final UserDataBase userData) {

        final HashMap<String, Double> favoriteMap = new HashMap<String, Double>();
        final MyHashMap map = new MyHashMap(favoriteMap);

        List<Show> videos = setVideoType(command);

        if (command.getActionType().equals("query")) {
            videos = applyFilterVideos(videos, command);
        }

        for (Show video : videos) {
            for (User user : userData.getUsersData()) {
                if (user.isAlreadyFav(video.getTitle())) {
                    Double success = map.isInMap(video.getTitle());
                    if (success != 0.0) {
                        favoriteMap.replace(video.getTitle(), success + 1);
                    } else {
                        favoriteMap.put(video.getTitle(), 1.0);
                    }
                }
            }
        }
        return favoriteMap;
    }

    /**
     * Creates a HashMap of video titles based on the video duration.
     *
     * @param command current action
     * @return a HashMap ---> video title | duration
     */
    public HashMap<String, Double> getLongestMap(final ActionInputData command) {
        final HashMap<String, Double> longestMap = new HashMap<String, Double>();

        List<Show> videos = setVideoType(command);
        videos = applyFilterVideos(videos, command);

        for (Show video : videos) {
            Double time = Double.valueOf(video.getDuration());
            longestMap.put(video.getTitle(), time);
        }

        return longestMap;
    }

    /**
     * Creates a list of all video titles from video database.
     *
     * @return a List of video titles
     */
    public List<String> createNameList() {
        List<String> videoNames = new ArrayList<>();
        for (Show video : videosData) {
            videoNames.add(video.getTitle());
        }
        return videoNames;
    }

}