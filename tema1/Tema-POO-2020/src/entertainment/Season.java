package entertainment;

import java.util.ArrayList;
import java.util.List;

/**
 * Information about a season of a tv show
 * <p>
 * DO NOT MODIFY
 */
public final class Season {
    /**
     * Number of current season
     */
    private final int currentSeason;
    /**
     * Duration in minutes of a season
     */
    private int duration;
    /**
     * List of ratings for each season
     */
    private List<Double> ratings;
    private List<String> usersRating;

    public List<String> getUsersRating() {
        return usersRating;
    }

    public void setUsersRating(List<String> usersRating) {
        this.usersRating = usersRating;
    }

    public Season(final int currentSeason, final int duration) {
        this.currentSeason = currentSeason;
        this.duration = duration;
        this.ratings = new ArrayList<>();
        this.usersRating = new ArrayList<>();
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(final int duration) {
        this.duration = duration;
    }

    public List<Double> getRatings() {
        return ratings;
    }

    public void setRatings(final List<Double> ratings) {
        this.ratings = ratings;
    }

    @Override
    public String toString() {
        return "Episode{" + "currentSeason=" + currentSeason + ", duration=" + duration + '}';
    }
}
