package dataset;


import java.util.List;
import actor.Actor;


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
}