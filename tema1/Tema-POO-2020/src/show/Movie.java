package show;

import java.util.ArrayList;
import java.util.List;

public final class Movie extends Show {
    private final int duration;

    private final List<Double> ratings;
    private final List<String> usersRating;

    public Movie(final String title, final ArrayList<String> cast, final ArrayList<String> genres,
            final int year, final int duration) {
        super(title, year, cast, genres);
        this.duration = duration;
        this.ratings = new ArrayList<>();
        this.usersRating = new ArrayList<>();
    }

    public List<Double> getRatings() {
        return ratings;
    }

    public List<String> getUsersRating() {
        return usersRating;
    }

    @Override
    public int getDuration() {
        return duration;
    }

    @Override
    public Double getRating() {
        Double filmRating = 0.0;
        if (ratings.size() != 0) {
            Double totalRatings = 0.0;
            for (int i = 0; i < ratings.size(); i++) {
                totalRatings += ratings.get(i);
            }
            // ratingul filmului
            filmRating = totalRatings / ratings.size();
        }
        return filmRating;
    }
}