package dataset;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import action.MyHashMap;
import actor.Actor;
import actor.ActorsAwards;
import fileio.ActionInputData;
import show.Show;

/**
 * Information about all actors and methods of processing their data, retrieved
 * from input database
 */
public final class ActorDataBase {
    /**
     * Information about actors, saved from input
     */
    private final List<Actor> actorsData;

    public ActorDataBase(final List<Actor> actorsData) {
        this.actorsData = actorsData;
    }

    public List<Actor> getActorsData() {
        return actorsData;
    }

    /**
     * This method sets the number of rated videos for every actor from database.
     *
     * @param clear  indicator whether to reset the values (0) or to set the new
     *               values (1)
     * @param videos
     */
    public void setNrOfRatedVideos(final List<Show> videos, int clear) {
        for (Show video : videos) {

            // Get the video rating
            Double filmRating = video.getRating();
            if (filmRating != 0) {
                for (String actorName : video.getCast()) {
                    for (int i = 0; i < actorsData.size(); i++) {
                        if (actorsData.get(i).getName().equals(actorName)) {
                            if (clear != 0) {
                                actorsData.get(i).setIsRated(actorsData.get(i).getIsRated() + 1);
                            } else {
                                // Reset the value
                                actorsData.get(i).setIsRated(0);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Method that creates a HashMap for actors based on the rating of the videos
     * they starred in.
     *
     * @param videoData videos from input
     * @param command   current action
     * @return a HashMap ---> actor's name | average rating
     */
    public HashMap<String, Double> getAverageMap(final VideoDataBase videoData) {

        HashMap<String, Double> averageMap = new HashMap<String, Double>();
        MyHashMap map = new MyHashMap(averageMap);

        // List of all videos from input
        List<Show> videos = videoData.getVideosData();

        // Clear the fields in order to be reused
        int clear = 0;
        this.setNrOfRatedVideos(videos, clear);
        // Set the number of rated films
        clear = 1;
        this.setNrOfRatedVideos(videos, clear);

        for (Show video : videos) {
            Double filmRating = video.getRating();
            if (filmRating != 0) {
                for (String actorName : video.getCast()) {
                    for (int i = 0; i < actorsData.size(); i++) {
                        if (actorsData.get(i).getName().equals(actorName)) {
                            // Check if the key already exists in HashMap
                            Double success = map.isInMap(actorsData.get(i).getName());
                            if (success != 0.0) {

                                Double finalRating = filmRating / actorsData.get(i).getIsRated();
                                Double newRating = success + finalRating;
                                // Update the value
                                averageMap.replace(actorsData.get(i).getName(), newRating);

                            } else {
                                Double finalRating = filmRating / actorsData.get(i).getIsRated();
                                // Insert new key and value
                                averageMap.put(actorsData.get(i).getName(), finalRating);

                            }
                        }
                    }
                }
            }

        }
        return averageMap;
    }

    /**
     * Method that creates a HashMap of actors based on the number of awards they
     * have, checking at the same time if they own the required prizes mentioned in
     * the action filter.
     *
     * @param command current action
     * @return a HashMap ---> actor's name | number of awards
     */
    public HashMap<String, Double> getAwardsMap(final ActionInputData command) {
        HashMap<String, Double> awardsMap = new HashMap<String, Double>();

        for (Actor actor : actorsData) {
            // Map that contains all actor's awards
            Map<ActorsAwards, Integer> hisAwards = actor.getAwards();

            Double nrAwards = 0.0;
            int hasAllAwards = 0;
            final int filterIndex = 3;
            List<String> commandAwards = command.getFilters().get(filterIndex);

            // Check if exists all mandatory awards
            for (String awardName : commandAwards) {
                for (Map.Entry<ActorsAwards, Integer> entry : hisAwards.entrySet()) {
                    nrAwards += Double.valueOf(entry.getValue());
                    if (awardName.equals(entry.getKey().toString())) {
                        hasAllAwards++;
                    }
                }
            }

            if (hasAllAwards == commandAwards.size()) {
                // The actor has all required awards
                awardsMap.put(actor.getName(), nrAwards);
            }
        }
        return awardsMap;
    }

    /**
     * Method that searches for some given words from action and creates a HashMap
     * with all the actors that have in their description career all the mentioned
     * words. When the requirements are met, the name of the actor will be stored in
     * an array that represents the final result.
     *
     * @param command current action
     * @return the final message to be displayed
     */
    public String getFilterDescription(final ActionInputData command) {
        String message;
        ArrayList<String> nameList = new ArrayList<String>();

        for (Actor actor : actorsData) {
            String description = actor.getCareerDescription();

            // Get the array with all the words to be searched for
            List<String> searchWords = command.getFilters().get(2);

            int hasAllWords = 0;
            for (int i = 0; i < searchWords.size(); i++) {
                // Create regex
                Pattern pattern = Pattern.compile(
                        "[ ,!.'()-]" + searchWords.get(i) + "[ ,!.'()-]",
                        Pattern.CASE_INSENSITIVE);
                Matcher m = pattern.matcher(description);
                if (m.find()) {
                    hasAllWords++;
                }
            }

            if (hasAllWords == searchWords.size()) {
                // The career description contains all required words
                nameList.add(actor.getName());
            }
        }

        // Sort the list of results
        if (command.getSortType().equals("asc")) {
            Collections.sort(nameList);
        } else {
            Collections.sort(nameList, Collections.reverseOrder());
        }

        // Create final message
        String tempMessage = nameList.toString();
        message = "Query result: " + tempMessage;

        return message;
    }
}
