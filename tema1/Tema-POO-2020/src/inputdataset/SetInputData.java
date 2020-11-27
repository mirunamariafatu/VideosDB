package inputdataset;

import java.util.ArrayList;
import java.util.List;

import actor.Actor;
import fileio.UserInputData;
import show.Movie;
import show.Serial;
import show.Show;
import dataset.ActorDataBase;
import dataset.UserDataBase;
import dataset.VideoDataBase;
import fileio.ActorInputData;
import fileio.Input;
import fileio.MovieInputData;
import fileio.SerialInputData;
import user.PremiumUser;
import user.User;

public final class SetInputData {

    private final Input input;

    public SetInputData(final Input input) {
        this.input = input;
    }

    public UserDataBase setUsersData() {

        List<UserInputData> tempUsersData = input.getUsers();
        List<User> users = new ArrayList<User>();

        for (UserInputData user : tempUsersData) {
            if (user.getSubscriptionType().equals("BASIC")) {
                User newUser = new User(user.getUsername(), user.getSubscriptionType(),
                        user.getHistory(), user.getFavoriteMovies());
                users.add(newUser);
            } else {
                User newUser = new PremiumUser(user.getUsername(), user.getSubscriptionType(),
                        user.getHistory(), user.getFavoriteMovies());
                users.add(newUser);
            }
        }

        UserDataBase userData = new UserDataBase(users);

        return userData;
    }

    public ActorDataBase setActorData() {
        List<ActorInputData> tempActorsData = input.getActors();
        List<Actor> actors = new ArrayList<Actor>();

        for (ActorInputData actor : tempActorsData) {
            Actor newActor = new Actor(actor.getName(), actor.getCareerDescription(),
                    actor.getFilmography(), actor.getAwards());
            actors.add(newActor);
        }

        ActorDataBase actorData = new ActorDataBase(actors);

        return actorData;
    }

    public VideoDataBase setVideolData() {
        List<MovieInputData> tempMoviesData = input.getMovies();
        List<SerialInputData> tempSerialsData = input.getSerials();
        List<Show> videos = new ArrayList<Show>();
        List<Show> serials = new ArrayList<Show>();
        List<Show> movies = new ArrayList<Show>();

        for (MovieInputData movie : tempMoviesData) {
            Show newVideo = new Movie(movie.getTitle(), movie.getCast(), movie.getGenres(),
                    movie.getYear(), movie.getDuration());
            movies.add(newVideo);
            videos.add(newVideo);
        }
        for (SerialInputData serial : tempSerialsData) {
            Show newVideo = new Serial(serial.getTitle(), serial.getCast(), serial.getGenres(),
                    serial.getNumberSeason(), serial.getSeasons(), serial.getYear());
            serials.add(newVideo);
            videos.add(newVideo);
        }

        VideoDataBase videoData = new VideoDataBase(videos, movies, serials);

        return videoData;
    }

}
