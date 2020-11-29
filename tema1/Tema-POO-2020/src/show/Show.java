package show;

import java.util.ArrayList;

/**
 * General information about show (video), retrieved from input
 */
public abstract class Show {
    /**
     * Show's title
     */
    private final String title;
    /**
     * The year the show was released
     */
    private final int year;
    /**
     * Show casting
     */
    private final ArrayList<String> cast;
    /**
     * Show genres
     */
    private final ArrayList<String> genres;

    public Show(final String title, final int year, final ArrayList<String> cast,
            final ArrayList<String> genres) {
        this.title = title;
        this.year = year;
        this.cast = cast;
        this.genres = genres;
    }

    public final String getTitle() {
        return title;
    }

    public final int getYear() {
        return year;
    }

    public final ArrayList<String> getCast() {
        return cast;
    }

    public final ArrayList<String> getGenres() {
        return genres;
    }

    /**
     * Method that calculates the final rating of the current video based on ratings
     * recieved from users
     *
     * @return final video rating
     */
    public abstract Double getRating();

    /**
     * Method that calculates the entire duration of the video
     *
     * @return duration of the video
     */
    public abstract int getDuration();

}
