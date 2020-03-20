package Othello;

import javafx.scene.paint.Color;

/**
 * The referee class mediates the moves between the players and determines when
 * the game is over. It also determines whether a move can be made and flips
 * pieces after a player makes a move.
 */
public class Referee {

	private Game _game;
	private Tile[][] _board;
	private Player _blackPlayer;
	private Player _whitePlayer;
	private Player _currentPlayer;

	/**
	 * The constructor simply stores different variables the referee will need.
	 */
	public Referee(Game game) {
		_game = game;
		_board = _game.getBoard();
	}

	/**
	 * This is called when the apply settings button is pressed. The Referee
	 * retrieves the players and starts the black players turn.
	 */
	public void newGame() {
		_blackPlayer = _game.getBlackPlayer();
		_whitePlayer = _game.getWhitePlayer();
		_currentPlayer = _blackPlayer;
		_currentPlayer.startTurn();
	}

	/**
	 * This is used by methods that need to access an opposite color, like the
	 * computer player for the minimax algorithm.
	 */
	public Color getOppositeColor(Color color) {
		if (color == Color.BLACK) {
			return Color.WHITE;
		} else {
			return Color.BLACK;
		}
	}

	/**
	 * This is called at the end of every players turn - human or computer. It
	 * first checks if the game is over, then changes the players turn if its
	 * not.
	 */
	public void switchPlayerTurn() {
		_game.updateScore();
		if (!this.playerHasValidMove(Color.BLACK, _board)
				&& !this.playerHasValidMove(Color.WHITE, _board)) {
			_game.endGame();
		} else {
			if (_currentPlayer == _blackPlayer) {
				_currentPlayer = _whitePlayer;
			} else {
				_currentPlayer = _blackPlayer;
			}
			_game.updateInstructionLabel(_currentPlayer.getPlayerColor());
			_currentPlayer.startTurn();
		}
	}

	/**
	 * This method is used by the players to determine if they must skip their
	 * turn or not and by the referee at the switch turn method to check if
	 * neither player can move, ending the game. It checks whether the player
	 * has any valid sandwiches.
	 */
	public boolean playerHasValidMove(Color playerColor, Tile[][] board) {
		boolean validMove = false;
		for (int i = 1; i < 9; i++) {
			for (int j = 1; j < 9; j++) {
				if (this.checkSandwich(playerColor, board, i, j)) {
					validMove = true;
				}
			}
		}
		return validMove;
	}

	/**
	 * This method loops through every direction for every position on the
	 * board, checking if the position has any sandwich. It is important for
	 * both player classes.
	 */
	public boolean checkSandwich(Color playerColor, Tile[][] board, int row,
			int col) {
		boolean sandwich = false;
		if (!board[row][col].getPiece().isVisible()) {
			for (int directionA = -1; directionA < 2; directionA++) {
				for (int directionB = -1; directionB < 2; directionB++) {
					int row2 = row + directionA;
					int col2 = col + directionB;
					boolean flag = false;
					while (true) {
						if (row2 == row && col2 == col) {
							break;
						}
						if (board[row2][col2] == null
								|| !board[row2][col2].getPiece().isVisible()) {
							break;
						} else if (board[row2][col2].getPiece().getFill() == this
								.getOppositeColor(playerColor)) {
							row2 = row2 + directionA;
							col2 = col2 + directionB;
							flag = true;
						} else if (board[row2][col2].getPiece().getFill() == playerColor) {
							if (flag) {
								sandwich = true;
								break;
							}
							break;
						}
					}
				}
			}
		}
		return sandwich;
	}

	/**
	 * This method is very similar to the checkSandwiches method but it actually
	 * goes back and flips the sandwiches too.
	 */
	public void flipSandwiches(Color playerColor, Tile[][] board, int row,
			int col) {
		for (int directionA = -1; directionA < 2; directionA++) {
			for (int directionB = -1; directionB < 2; directionB++) {
				boolean sandwich = false;
				int row2 = row + directionA;
				int col2 = col + directionB;
				int x = 0;
				boolean flag = false;
				while (true) {
					// avoids edge case of direction being 0, 0.
					if (row == row2 && col == col2) {
						break;
					}
					if (board[row2][col2] == null
							|| !board[row2][col2].getPiece().isVisible()) {
						if (flag) {
						}
						break;
					}
					if (board[row2][col2].getPiece().getFill() == this
							.getOppositeColor(playerColor)) {

						row2 = row2 + directionA;
						col2 = col2 + directionB;
						x++;
						flag = true;
					} else if (board[row2][col2].getPiece().getFill() == playerColor) {
						if (flag) {
							sandwich = true;
							break;
						}
						break;
					}
					continue;
				}
				if (sandwich) {

					for (int i = 0; i < x; i++) {
						row2 = row2 - directionA;
						col2 = col2 - directionB;
						board[row2][col2].getPiece().setFill(playerColor);
					}
				}
			}
		}
	}
}