package user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


import dataset.VideoDataBase;
import fileio.ActionInputData;
import show.Show;

public final class PremiumUser extends User {

    public PremiumUser(final String username, final String subscriptionType,
            final Map<String, Integer> history, final ArrayList<String> favoriteMovies) {
        super(username, subscriptionType, history, favoriteMovies);
    }

}