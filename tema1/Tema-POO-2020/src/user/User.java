package user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import action.MyHashMap;
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
	/**
	 * Number of ratings given by this user
	 */
	private int ratingNr = 0;

	public User(final String username, final String subscriptionType, final Map<String, Integer> history,
			final ArrayList<String> favoriteMovies) {
		this.username = username;
		this.subscriptionType = subscriptionType;
		this.favoriteMovies = favoriteMovies;
		this.history = history;
	}

	public final int getRatingNr() {
		return ratingNr;
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

	/**
	 * Method that adds a video to the user's favorites list. If the current show is
	 * already favorite, an error message is displayed.
	 *
	 * @param command current action
	 * @return final message to be displayed
	 */
	public final String add2favorites(final ActionInputData command) {
		String message;

		// Check if the video is seen by the user
		if (isInHistory(command.getTitle())) {

			// Check if the video is already in favorites list
			if (isAlreadyFav(command.getTitle())) {
				message = "error -> " + command.getTitle() + " is already in favourite list";
			} else {

				// Add the video to favorites list
				favoriteMovies.add(command.getTitle());
				message = "success -> " + command.getTitle() + " was added as favourite";
			}

		} else {
			message = "error -> " + command.getTitle() + " is not seen";
		}
		return message;
	}

	/**
	 * Method that marks a video as seen by a user, and counts the number of views
	 *
	 * @param command current action
	 * @return the final message to be displayed
	 */
	public final String videoView(final ActionInputData command) {
		String message;

		// Check if the video is seen by the user
		if (isInHistory(command.getTitle())) {

			// Update the number of views
			int newValue = history.get(command.getTitle()) + 1;
			history.replace(command.getTitle(), newValue);
			message = "success -> " + command.getTitle() + " was viewed with total views of " + newValue;
		} else {

			// Mark the video as seen
			history.put(command.getTitle(), 1);
			message = "success -> " + command.getTitle() + " was viewed with total views of " + 1;
		}
		return message;
	}

	/**
	 * Method that saves the rating given by the user in videos' database
	 *
	 * @param command current action
	 * @param videos  information about videos'database
	 * @return
	 */
	public final String giveRating(final ActionInputData command, final VideoDataBase videos) {
		String message = "";
		int isSerial = 0;

		// Check if the video is seen by the user
		if (isInHistory(command.getTitle())) {

			// Search for the category the video belongs to
			for (int i = 0; i < videos.getSerialsData().size(); i++) {
				if (videos.getSerialsData().get(i).getTitle().equals(command.getTitle())) {
					// Video --> is serial
					isSerial = 1;
					message = serialRating(videos.getSerialsData().get(i), command);
				}
			}

			if (isSerial == 0) {
				// Video --> is movie
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

	/**
	 * Method that sets the serial's rating to its database, considering that a user
	 * can only rate a season once
	 *
	 * @param serial  serial to be rated
	 * @param command current action
	 * @return the final message to be displayed
	 */
	public final String serialRating(final Show serial, final ActionInputData command) {
		int foundName = 0;
		String message = "";

		List<String> usersWhoRated = ((Serial) serial).getSeasons().get(command.getSeasonNumber() - 1).getUsersRating();
		List<Double> ratingList = ((Serial) serial).getSeasons().get(command.getSeasonNumber() - 1).getRatings();

		if (usersWhoRated != null) {
			for (int i = 0; i < usersWhoRated.size(); i++) {
				String tempUsername = ((Serial) serial).getSeasons().get(command.getSeasonNumber() - 1).getUsersRating()
						.get(i);

				// Check if the user already rated this season
				if (tempUsername.equals(command.getUsername())) {
					foundName = 1;
					message = "error -> " + command.getTitle() + " has been already rated";
				}
			}
		}

		if (foundName == 0) {
			// This user has not rated this season yet
			ratingNr += 1;
			usersWhoRated.add(command.getUsername());
			ratingList.add(command.getGrade());
			message = "success -> " + command.getTitle() + " was rated with " + command.getGrade() + " by "
					+ command.getUsername();
		}

		return message;
	}

	/**
	 * Method that sets the movie's rating to its database, considering that a user
	 * can only rate a movie once
	 *
	 * @param movie   movie to be rated
	 * @param command current action
	 * @return the final message to be displayed
	 */
	public final String movieRating(final Show movie, final ActionInputData command) {
		String message = "";
		int foundName = 0;

		List<String> usersWhoRated = ((Movie) movie).getUsersRating();
		List<Double> ratingList = ((Movie) movie).getRatings();

		for (int i = 0; i < usersWhoRated.size(); i++) {
			// Check if the user already rated this movie
			String tempUsername = ((Movie) movie).getUsersRating().get(i);
			if (tempUsername.equals(command.getUsername())) {
				foundName = 1;
				message = "error -> " + command.getTitle() + " has been already rated";
			}
		}

		if (foundName == 0) {
			// This user has not rated this season yet
			ratingNr += 1;
			usersWhoRated.add(command.getUsername());
			ratingList.add(command.getGrade());
			message = "success -> " + command.getTitle() + " was rated with " + command.getGrade() + " by "
					+ command.getUsername();
		}
		return message;
	}

	/**
	 * Method that creates a list of all video titles in the user's history
	 *
	 * @return a list of titles
	 */
	public final ArrayList<String> getHistoryVideos() {
		ArrayList<String> viewedVideos = new ArrayList<String>();

		for (Map.Entry<String, Integer> entry : history.entrySet()) {
			viewedVideos.add(entry.getKey());
		}

		return viewedVideos;
	}

	/**
	 * This method verifies a video has been viewed by the current user, that is, if
	 * the video title is in the user's history or not
	 *
	 * @param title video title to be verified
	 * @return true --> the video is in history; false --> the video is not seen yet
	 */
	public final boolean isInHistory(final String title) {
		ArrayList<String> viewedVideos = getHistoryVideos();

		for (String videoName : viewedVideos) {
			if (videoName.equals(title)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * This method checks if the movie is already in the user's favorites list or
	 * not
	 *
	 * @param title video title to be verified
	 * @return true --> the video is already in favorites list; false --> video is
	 *         not favorite
	 */
	public final boolean isAlreadyFav(final String title) {
		for (int i = 0; i < favoriteMovies.size(); i++) {
			if (favoriteMovies.get(i).equals(title)) {
				return true;
			}
		}

		return false;
	}
}
