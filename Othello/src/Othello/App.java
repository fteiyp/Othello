package Othello;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This is the app class. This is where is all starts
 */
public class App extends Application {

	/**
	 * start method of the App class begins entire application.
	 */
	@Override
	public void start(Stage stage) {
		stage.setTitle("Othello!");
		PaneOrganizer organizer = new PaneOrganizer();
		Scene scene = new Scene(organizer.getBorderPane());
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] argv) {
		// launch is a method inherited from Application
		launch(argv);
	}
}
