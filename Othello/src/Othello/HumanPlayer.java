package Othello;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * The human player class is much more simple that the computer player class. It
 * just uses a click handler to retrieve where moves go, and defines only the
 * three methods required by the player parent class.
 */
public class HumanPlayer extends Player {

	private EventHandler<MouseEvent> _clickHandler;
	private Pane _boardPane;

	/**
	 * The constructor stores the board pane and passes the game and color into
	 * super to also store the game and color (protected instance variables).
	 */
	public HumanPlayer(Game game, Color color) {
		super(game, color);
		_boardPane = _game.getBoardPane();
	}

	/**
	 * Tells player to move if it has a move, otherwise end its turn.
	 */
	@Override
	public void startTurn() {
		if (_referee.playerHasValidMove(_playerColor, _board)) {
			this.move();
		} else {
			this.endTurn();
		}
	}

	/**
	 * Changes the color of all possible moves to grey so the player knows where
	 * they can go. Then creates a clickhandler for placing the piece, then
	 * calls flipSandwiches.
	 */
	@Override
	public void move() {
		for (int row = 1; row < 9; row++) {
			for (int col = 1; col < 9; col++) {
				if (_referee.checkSandwich(_playerColor, _board, row, col)) {
					_board[row][col].getTile().setFill(Color.GREY);
				}
			}
		}
		_boardPane.addEventHandler(MouseEvent.MOUSE_CLICKED,
				_clickHandler = new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						int row = (int) event.getSceneY()
								/ Constants.SQUARE_SIZE;
						int col = (int) event.getSceneX()
								/ Constants.SQUARE_SIZE;
						if (0 < col && col < 9 && 0 < row && row < 9) {
							if (_board[row][col].getTile().getFill() == Color.GREY) {
								// places piece
								_game.getBoard()[row][col].getPiece().setFill(
										_playerColor);
								_game.getBoard()[row][col].getPiece()
										.setVisible(true);
								// flips sandwiched pieces
								_referee.flipSandwiches(_playerColor, _board,
										row, col);
								endTurn();
								event.consume();
							}
						}
					}
				});
	}

	/**
	 * End turn flips any grey square back to green before the other players
	 * turn begins. Then calls the referee to switch players.
	 */
	@Override
	public void endTurn() {
		_boardPane.removeEventHandler(MouseEvent.MOUSE_CLICKED, _clickHandler);
		for (int row = 1; row < 9; row++) {
			for (int col = 1; col < 9; col++) {
				if (_board[row][col].getTile().getFill() == Color.GREY) {
					_board[row][col].getTile().setFill(Color.GREEN);
				}
			}
		}
		_game.getReferee().switchPlayerTurn();
	}
}
