package action;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import dataset.VideoDataBase;
import fileio.ActionInputData;

/**
 * Class that contains methods for sorting and analyzing a HashMap
 */
public final class MyHashMap {

    /**
     * Map on which sorting will be performed
     */
    private HashMap<String, Double> map;

    private VideoDataBase videoData;

    public MyHashMap(final HashMap<String, Double> map) {
        this.map = map;
    }

    public MyHashMap(final HashMap<String, Double> map, final VideoDataBase videoData) {
        this.map = map;
        this.videoData = videoData;
    }

    /**
     * A comparison method, which imposes a total ordering for the objects in the
     * hashmap First comparison criteria is by the values in the hashmap Second
     * comparison criteria is lexicographical order by key
     */
    class ComparaFilterName implements Comparator<Map.Entry<String, Double>> {
        public int compare(final Map.Entry<String, Double> o1,
                final Map.Entry<String, Double> o2) {

            int result = o1.getValue().compareTo(o2.getValue());
            if (result == 0) {
                String actor1 = o1.getKey();
                String actor2 = o2.getKey();
                result = actor1.compareTo(actor2);
            }
            return result;
        }
    }

    /**
     * A comparison method, which imposes a total ordering for the objects in the
     * hashmap First comparison criteria is by the values in the hashmap Second
     * comparison criteria is the insertion order in database
     */
    class ComparaFilterIndex implements Comparator<Map.Entry<String, Double>> {
        public int compare(final Map.Entry<String, Double> o1,
                final Map.Entry<String, Double> o2) {
            int result = o1.getValue().compareTo(o2.getValue());
            if (result == 0) {

                Integer idx1 = videoData.createNameList().indexOf(o1.getKey());
                Integer idx2 = videoData.createNameList().indexOf(o2.getKey());
                // Insertion order will always be ascending
                result = idx2 - idx1;
            }
            return result;
        }
    }

    /**
     * Method that creates and returns a HashMap sorting the values from the current
     * HashMap using different comparison filters and methods
     *
     * @param command current action
     * @return sorted map
     */
    public HashMap<String, Double> sortMapByValue(final ActionInputData command) {
        // Create a list from elements of HashMap
        List<Map.Entry<String, Double>> list = new LinkedList<Map.Entry<String, Double>>(
                map.entrySet());

        // Sort the list
        if (command.getActionType().equals("query")) {

            ComparaFilterName comparator = new ComparaFilterName();
            if (command.getSortType().equals("asc")) {
                // Ascending sort
                Collections.sort(list, comparator);
            } else {
                // Descending sort
                Collections.sort(list, comparator.reversed());
            }

        } else {
            if (command.getType().equals("best_unseen") || command.getType().equals("favorite")) {

                ComparaFilterIndex comparator = new ComparaFilterIndex();
                Collections.sort(list, comparator.reversed());

            } else if (command.getType().equals("popular")) {

                ComparaFilterName comparator = new ComparaFilterName();
                Collections.sort(list, comparator.reversed());

            } else if (command.getType().equals("search")) {

                ComparaFilterName comparator = new ComparaFilterName();
                Collections.sort(list, comparator);

            }
        }

        // Put data from sorted list to hashmap
        HashMap<String, Double> temp = new LinkedHashMap<String, Double>();
        for (Map.Entry<String, Double> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }

        // Return sorted map
        return temp;
    }

    /**
     * Method that searches for a key (video title) in the HashMap
     *
     * @param videoName string (key) to be searched in the hashmap
     * @return 0 -> if the given video name (key) is not found in the hashmap old
     *         value -> if the given video name (key) is found
     */
    public Double isInMap(final String videoName) {

        for (Iterator<Entry<String, Double>> iterator = map.entrySet().iterator(); iterator
                .hasNext();) {

            Entry<String, Double> mapElement = iterator.next();
            String video = (String) mapElement.getKey();

            if (video.equals(videoName)) {
                // Key is already in map
                Double oldValue = (Double) mapElement.getValue();
                return oldValue;
            }

        }
        return 0.0;
    }

}
