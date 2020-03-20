package Othello;

import javafx.scene.paint.Color;

/**
 * This class is abstract and is inherited by the computer player and human
 * player. It requires that they define the startTurn, endTurn, and move
 * methods. It also has the protected instance variable color, game, ref, board,
 * for either class to store on instantiation.
 */
public abstract class Player {
	protected Color _playerColor;
	protected Game _game;
	protected Referee _referee;
	protected Tile[][] _board;

	/**
	 * The constructor stores the information given to it by the game class
	 * after apply settings is pressed.
	 */
	public Player(Game game, Color color) {
		_game = game;
		_playerColor = color;
		_referee = _game.getReferee();
		_board = _game.getBoard();
	}

	/**
	 * Starts either player types turn
	 */
	public void startTurn() {
	}

	/**
	 * Ends either player types turn
	 */
	public void endTurn() {
	}

	/**
	 * Very different ways of defining move for computer player and human player
	 * but they both override and define it.
	 */
	public void move() {
	}

	/**
	 * Useful getter method for retrieving a players color - used in getBestMove
	 * computer player method.
	 */
	public Color getPlayerColor() {
		return _playerColor;
	}
}
