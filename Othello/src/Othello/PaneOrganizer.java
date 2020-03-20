package Othello;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

/**
 * The pane organizer mostly just starts up the game class and organizes the
 * settings pane and board pane into a larger boarder pane. Reset game gets
 * called by the game class when the reset button is pressed.
 */
public class PaneOrganizer {
	private BorderPane _borderPane;
	private Game _game;

	/**
	 * The constructor makes the boarder pane that organizes the two mane panes
	 * made in the game class. It also instantiates the game,
	 */
	public PaneOrganizer() {
		_borderPane = new BorderPane();
		_borderPane.setPrefSize(Constants.SQUARE_SIZE * 14,
				Constants.SQUARE_SIZE * 10);
		_game = new Game(this);
		_borderPane.setCenter(_game.getBoardPane());
		_borderPane.setRight(_game.getSettingsPane());
	}

	/**
	 * This method is called by the game class when the reset button is clicked.
	 */
	public void resetGame() {
		_game = new Game(this);
		_borderPane.setCenter(_game.getBoardPane());
		_borderPane.setRight(_game.getSettingsPane());
	}

	/**
	 * This is a necessary accessor method for the App class to be able to
	 * display the boarder pane.
	 */
	public Pane getBorderPane() {
		return _borderPane;
	}
}
