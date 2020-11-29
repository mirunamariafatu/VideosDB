package action;

import dataset.VideoDataBase;
import fileio.ActionInputData;

import user.User;


/**
 * Class that deals with the processing of "command" type actions
 */
public final class Command {
    /**
     * Current user
     */
    private User user;
    /**
     * Current action
     */
    private ActionInputData command;
    /**
     * Message to be created
     */
    private String message;
    /**
     * All videos from input
     */
    private final VideoDataBase videos;

    public Command(final User user, final ActionInputData command, final VideoDataBase videos) {
        this.command = command;
        this.user = user;
        this.videos = videos;
    }

    /**
     * This method checks the type of the given action and calls the required
     * function in order to obtain the final message.
     *
     * @return the final message to be displayed
     */
    public String getMessage() {
        switch (command.getType()) {
        case "favorite":
            message = user.add2favorites(command);
            break;
        case "view":
            message = user.videoView(command);
            break;
        case "rating":
            message = user.giveRating(command, videos);
            break;
        default:
            message = "Invalid command !";
        }

        return message;
    }

}
