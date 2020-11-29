package action;

import dataset.UserDataBase;
import user.PremiumUser;
import dataset.VideoDataBase;
import fileio.ActionInputData;
import user.User;

/**
 * Class that deals with the processing of "recommendation" type actions
 */
public final class Recommendation {
    /**
     * Information from input (database) on which will be performed different actions
     */
    private VideoDataBase videoData;
    private UserDataBase userData;
    private User user;

    private String message;
    private ActionInputData command;

    public Recommendation(final User user, final ActionInputData command,
            final VideoDataBase videoData, final UserDataBase userData) {
        this.command = command;
        this.user = user;
        this.videoData = videoData;
        this.userData = userData;
    }

    /**
     * Method that checks the type of the recommendation, checking at the same time
     * whether the user is PREMIUM or BASIC.
     *
     * @return the message to be displayed
     */
    public String getMessage() {

        switch (command.getType()) {

        case "standard":
            message = user.getStandardVideo(videoData);
            break;

        case "best_unseen":
            message = user.getBestUnseenVideo(videoData, command);
            break;

        case "popular":
            if (user.getSubscriptionType().equals("PREMIUM")) {
                message = ((PremiumUser) user).getPopularVideo(userData, videoData, command);
            } else {
                message = "PopularRecommendation cannot be applied!";
            }
            break;

        case "favorite":
            if (user.getSubscriptionType().equals("PREMIUM")) {
                message = ((PremiumUser) user).getFavoriteVideo(userData, videoData, command);
            } else {
                message = "FavoriteRecommendation cannot be applied!";
            }
            break;

        case "search":
            if (user.getSubscriptionType().equals("PREMIUM")) {
                message = ((PremiumUser) user).getSearchVideo(userData, videoData, command);
            } else {
                message = "SearchRecommendation cannot be applied!";
            }
            break;
        default:
            message = "Invalid command !";
        }

        return message;
    }
}
