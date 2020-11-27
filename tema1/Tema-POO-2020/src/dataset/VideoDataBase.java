package dataset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import fileio.ActionInputData;
import show.Show;
import user.User;

public final class VideoDataBase {
	private final List<Show> videosData;
	private final List<Show> moviesData;
	private final List<Show> serialsData;

	public VideoDataBase(final List<Show> videosData, final List<Show> moviesData, final List<Show> serialsData) {
		this.videosData = videosData;
		this.moviesData = moviesData;
		this.serialsData = serialsData;
	}

	public List<Show> getMoviesData() {
		return moviesData;
	}

	public List<Show> getSerialsData() {
		return serialsData;
	}

	public List<Show> getVideosData() {
		return videosData;
	}
}
