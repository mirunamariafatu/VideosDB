package main;

import checker.Checkstyle;
import checker.Checker;
import common.Constants;
import dataset.ActorDataBase;
import dataset.UserDataBase;
import dataset.VideoDataBase;
import fileio.ActionInputData;
import fileio.Input;
import fileio.InputLoader;
import fileio.Writer;
import inputdataset.SetInputData;
import user.User;

import org.json.simple.JSONArray;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import org.json.simple.JSONObject;

import action.Command;
import action.Query;

/**
 * The entry point to this homework. It runs the checker that tests your
 * implentation.
 */
public final class Main {
	/**
	 * for coding style
	 */
	private Main() {
	}

	/**
	 * Call the main checker and the coding style checker
	 *
	 * @param args from command line
	 * @throws IOException in case of exceptions to reading / writing
	 */
	public static void main(final String[] args) throws IOException {
		File directory = new File(Constants.TESTS_PATH);
		Path path = Paths.get(Constants.RESULT_PATH);
		if (!Files.exists(path)) {
			Files.createDirectories(path);
		}

		File outputDirectory = new File(Constants.RESULT_PATH);

		Checker checker = new Checker();
		checker.deleteFiles(outputDirectory.listFiles());

		for (File file : Objects.requireNonNull(directory.listFiles())) {

			String filepath = Constants.OUT_PATH + file.getName();
			File out = new File(filepath);
			boolean isCreated = out.createNewFile();
			if (isCreated) {
				action(file.getAbsolutePath(), filepath);
			}
		}

		checker.iterateFiles(Constants.RESULT_PATH, Constants.REF_PATH, Constants.TESTS_PATH);
		Checkstyle test = new Checkstyle();
		test.testCheckstyle();
	}

	/**
	 * @param filePath1 for input file
	 * @param filePath2 for output file
	 * @throws IOException in case of exceptions to reading / writing
	 */
	public static void action(final String filePath1, final String filePath2) throws IOException {
		InputLoader inputLoader = new InputLoader(filePath1);
		Input input = inputLoader.readData();

		Writer fileWriter = new Writer(filePath2);
		JSONArray arrayResult = new JSONArray();

		// Set all informations from input to their databases
		SetInputData database = new SetInputData(input);
		UserDataBase users = database.setUsersData();
		ActorDataBase actors = database.setActorData();
		VideoDataBase videos = database.setVideolData();

		for (ActionInputData action : input.getCommands()) {

			// Check action type
			switch (action.getActionType()) {

			case "command":

				for (int i = 0; i < users.getUsersData().size(); i++) {
					// Get the targeted user
					if (action.getUsername().equals(users.getUsersData().get(i).getUsername())) {
						User myUser = users.getUsersData().get(i);
						Command newCommand = new Command(myUser, action, videos);
						// Get the final message
						String messageC = newCommand.getMessage();

						JSONObject obj = fileWriter.writeFile(action.getActionId(), "", messageC);
						arrayResult.add(obj);
					}
				}
				break;

			case "query":

				Query newQuery = new Query(actors, videos, action, users);
				// Get the final message
				String messageQ = newQuery.getQueryMessage();
				JSONObject obj = fileWriter.writeFile(action.getActionId(), "", messageQ);
				arrayResult.add(obj);
				break;

			default:
				String message = "Invalid command !";
			}

		}
		fileWriter.closeJSON(arrayResult);
	}
}
