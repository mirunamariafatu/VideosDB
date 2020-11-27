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

}
