package user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import dataset.UserDataBase;
import dataset.VideoDataBase;
import fileio.ActionInputData;
import show.Movie;
import show.Serial;
import show.Show;

public class User {
	/**
	 * User's username
	 */
	private final String username;
	/**
	 * Subscription Type
	 */
	private final String subscriptionType;
	/**
	 * The history of the movies seen
	 */
	private Map<String, Integer> history;
	/**
	 * Movies added to favorites
	 */
	private ArrayList<String> favoriteMovies;

	private int ratingNr = 0;

	public final int getRatingNr() {
		return ratingNr;
	}

	public User(final String username, final String subscriptionType, final Map<String, Integer> history,
			final ArrayList<String> favoriteMovies) {
		this.username = username;
		this.subscriptionType = subscriptionType;
		this.favoriteMovies = favoriteMovies;
		this.history = history;
	}

	public final String getUsername() {
		return username;
	}

	public final Map<String, Integer> getHistory() {
		return history;
	}

	public final String getSubscriptionType() {
		return subscriptionType;
	}

	public final ArrayList<String> getFavoriteMovies() {
		return favoriteMovies;
	}

	public final String add2favorites(final ActionInputData command) {

		String message;
		int isInHistory = isInHistory(command.getTitle());

		if (isInHistory != 0) {

			int isAlreadyFav = isAlreadyFav(command.getTitle());

			if (isAlreadyFav != 0) {
				message = "error -> " + command.getTitle() + " is already in favourite list";
			} else {
				favoriteMovies.add(command.getTitle());
				message = "success -> " + command.getTitle() + " was added as favourite";

			}

		} else {
			message = "error -> " + command.getTitle() + " is not seen";
		}

		return message;
	}

	public final String movieView(final ActionInputData command) {

		String message;
		int alreadyView = isInHistory(command.getTitle());

		if (alreadyView != 0) {

			int newValue = history.get(command.getTitle()) + 1;
			history.replace(command.getTitle(), newValue);

			message = "success -> " + command.getTitle() + " was viewed with total views of " + newValue;

		} else {
			history.put(command.getTitle(), 1);
			message = "success -> " + command.getTitle() + " was viewed with total views of " + 1;
		}

		return message;
	}

	public final String giveRating(final ActionInputData command, final VideoDataBase videos) {

		String message = "";
		int isSerial = 0;
		int alreadyView = isInHistory(command.getTitle());

		if (alreadyView != 0) {
			for (int i = 0; i < videos.getSerialsData().size(); i++) {
				if (videos.getSerialsData().get(i).getTitle().equals(command.getTitle())) {
					isSerial = 1;
					message = serialRating(videos.getSerialsData().get(i), command);
				}
			}
			if (isSerial == 0) {
				for (int i = 0; i < videos.getMoviesData().size(); i++) {
					if (videos.getMoviesData().get(i).getTitle().equals(command.getTitle())) {
						message = movieRating(videos.getMoviesData().get(i), command);
					}
				}
			}
		} else {
			message = "error -> " + command.getTitle() + " is not seen";
		}

		return message;
	}

	public final String serialRating(final Show serial, final ActionInputData command) {
		int foundName = 0;
		String message = "";

		if (((Serial) serial).getSeasons().get(command.getSeasonNumber() - 1).getUsersRating() != null) {
			for (int i = 0; i < ((Serial) serial).getSeasons().get(command.getSeasonNumber() - 1).getUsersRating()
					.size(); i++) {
				// caut daca a dat deja rating (adica numele lui e in lista)
				if (((Serial) serial).getSeasons().get(command.getSeasonNumber() - 1).getUsersRating().get(i)
						.equals(command.getUsername())) {
					foundName = 1;
					message = "error -> " + command.getTitle() + " has been already rated";
				}
			}
		}
		// nu a dat rating pana acum
		if (foundName == 0) {
			ratingNr = ratingNr + 1;
			((Serial) serial).getSeasons().get(command.getSeasonNumber() - 1).getUsersRating()
					.add(command.getUsername());
			((Serial) serial).getSeasons().get(command.getSeasonNumber() - 1).getRatings().add(command.getGrade());
			message = "success -> " + command.getTitle() + " was rated with " + command.getGrade() + " by "
					+ command.getUsername();
		}

		return message;
	}

	public final String movieRating(final Show movie, final ActionInputData command) {
		int foundName = 0;
		String message = "";

		for (int i = 0; i < ((Movie) movie).getUsersRating().size(); i++) {
			if (((Movie) movie).getUsersRating().get(i).equals(command.getUsername())) {
				foundName = 1;
				message = "error -> " + command.getTitle() + " has been already rated";
			}
		}

		if (foundName == 0) {
			ratingNr = ratingNr + 1;
			((Movie) movie).getUsersRating().add(command.getUsername());
			((Movie) movie).getRatings().add(command.getGrade());
			message = "success -> " + command.getTitle() + " was rated with " + command.getGrade() + " by "
					+ command.getUsername();
		}
		return message;
	}

	public final ArrayList<String> getHistoryVideos() {
		ArrayList<String> viewedVideos = new ArrayList<String>();

		for (Map.Entry<String, Integer> entry : history.entrySet()) {
			viewedVideos.add(entry.getKey());
		}

		return viewedVideos;
	}

	public final int isInHistory(final String title) {
		ArrayList<String> viewedVideos = getHistoryVideos();

		int isInHistory = 0;
		for (String videoName : viewedVideos) {
			if (videoName.equals(title)) {
				isInHistory = 1;
			}
		}

		return isInHistory;
	}

	public final int isAlreadyFav(final String title) {

		int isAlreadyFav = 0;
		for (int i = 0; i < favoriteMovies.size(); i++) {
			if (favoriteMovies.get(i).equals(title)) {
				isAlreadyFav = 1;
			}
		}

		return isAlreadyFav;
	}

}
