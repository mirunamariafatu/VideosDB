package actor;

import java.util.ArrayList;

import java.util.Map;

/**
 * Information about an actor, retrieved from parsing the input test files
 */
public final class Actor {
    /**
     * Actor name
     */
    private String name;
    /**
     * Description of the actor's career
     */
    private String careerDescription;
    /**
     * Videos starring actor
     */
    private ArrayList<String> filmography;
    /**
     * Awards won by the actor
     */
    private Map<ActorsAwards, Integer> awards;
    /**
     * Number of times a video in which the actor starred was rated
     */
    private int isRated;

    public Actor(final String name, final String careerDescription,
            final ArrayList<String> filmography, final Map<ActorsAwards, Integer> awards) {
        this.name = name;
        this.careerDescription = careerDescription;
        this.filmography = filmography;
        this.awards = awards;
        this.isRated = 0;
    }

    public int getIsRated() {
        return isRated;
    }

    public void setIsRated(int isRated) {
        this.isRated = isRated;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public ArrayList<String> getFilmography() {
        return filmography;
    }

    public void setFilmography(final ArrayList<String> filmography) {
        this.filmography = filmography;
    }

    public Map<ActorsAwards, Integer> getAwards() {
        return awards;
    }

    public String getCareerDescription() {
        return careerDescription;
    }

    public void setCareerDescription(final String careerDescription) {
        this.careerDescription = careerDescription;
    }
}
