package show;

import java.util.ArrayList;

import entertainment.Season;

/**
 * Information about a serial, retrieved from input
 */
public final class Serial extends Show {
    /**
     * Number of seasons
     */
    private final int numberOfSeasons;
    /**
     * Season list
     */
    private final ArrayList<Season> seasons;

    public Serial(final String title, final ArrayList<String> cast,
            final ArrayList<String> genres, final int numberOfSeasons,
            final ArrayList<Season> seasons, final int year) {
        super(title, year, cast, genres);
        this.numberOfSeasons = numberOfSeasons;
        this.seasons = seasons;
    }

    public int getNumberSeason() {
        return numberOfSeasons;
    }

    public ArrayList<Season> getSeasons() {
        return seasons;
    }

    @Override
    public Double getRating() {
        Double serialRating = 0.0;
        Double seasonRating = 0.0;
        Double totalRatings = 0.0;

        // Calculate the rating of each season
        for (Season season : seasons) {
            if (season.getRatings().size() != 0) {
                for (int i = 0; i < season.getRatings().size(); i++) {
                    totalRatings += season.getRatings().get(i);
                }
                seasonRating = totalRatings / season.getRatings().size();
                totalRatings = 0.0;
            } else {
                seasonRating = 0.0;
            }
            serialRating += seasonRating;
        }

        // Calculate the final serial rating
        serialRating = serialRating / numberOfSeasons;
        return serialRating;
    }

    @Override
    public int getDuration() {
        int time = 0;
        for (Season season : seasons) {
            time += season.getDuration();
        }
        return time;
    }

}
