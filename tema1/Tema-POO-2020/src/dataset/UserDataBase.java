package dataset;

import java.util.HashMap;
import java.util.List;
import user.User;

public final class UserDataBase {

	private final List<User> usersData;

	public UserDataBase(final List<User> usersData) {
		this.usersData = usersData;
	}

	public List<User> getUsersData() {
		return usersData;
	}
}