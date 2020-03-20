package Othello;

import java.util.ArrayList;
import java.util.Collections;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * The computer class makes moves using a recursive algorithm to evaluate
 * possible moves in the future. Its intelligence determines how far ahead it
 * will look.
 */
public class ComputerPlayer extends Player {

	private int _intelligence;
	private Timeline _timeline;
	private boolean _nondeterministic;
	private ArrayList<Move> _moveList;

	/**
	 * The constructor stores both its intelligence and whether it is
	 * deterministic or not. If it is nondeterministic, it will make moves of
	 * equal worth randomly. Its timeline creates a pause to let the other
	 * player see it make its move.
	 */
	ComputerPlayer(Game game, Color playerColor, int intelligence,
			boolean nondeterministic) {
		super(game, playerColor);
		_intelligence = intelligence;
		KeyFrame keyframe = new KeyFrame(Duration.seconds((int) _game
				.getSliderVariable()), new TimeHandler());
		_timeline = new Timeline(keyframe);
		_nondeterministic = nondeterministic;
		_moveList = new ArrayList<Move>();
	}

	/**
	 * The startturn method checks for a valid move like the human player and
	 * starts the timeline if one is available. Otherwise, it will end its turn.
	 */
	@Override
	public void startTurn() {
		if (_referee.playerHasValidMove(_playerColor, _board)) {
			_timeline.play();
		} else {
			this.endTurn();
		}
	}

	/**
	 * This simply calls on the referee to start the other players turn and
	 * update who is the current player.
	 */
	@Override
	public void endTurn() {
		_referee.switchPlayerTurn();
	}

	/**
	 * The move method calls on the getBestMove method which contains the
	 * minimax algorithm. The Move returned by the minimax algorithm is the move
	 * it makes. The move class simply stores the row, column and value of a
	 * move.
	 */
	@Override
	public void move() {
		_moveList = this.makeMoveList();
		Move move = this.getBestMove(_board, _intelligence, _playerColor);
		_board[move.getRow()][move.getCol()].getPiece().setFill(_playerColor);
		_board[move.getRow()][move.getCol()].getPiece().setVisible(true);
		_referee.flipSandwiches(_playerColor, _board, move.getRow(),
				move.getCol());
		this.endTurn();
	}

	/**
	 * The makeMoveList method makes an arraylist of all the moves on the board,
	 * and randomizes them for the minimax algorithm if the computer player is
	 * set to nondeterministic.
	 */
	public ArrayList<Move> makeMoveList() {
		ArrayList<Move> moveList = new ArrayList<Move>();
		for (int row = 1; row < 9; row++) {
			for (int col = 1; col < 9; col++) {
				Move move = new Move();
				move.setRow(row);
				move.setCol(col);
				moveList.add(move);
			}
		}
		if (_nondeterministic) {
			Collections.shuffle(moveList);
		}
		return moveList;
	}

	/**
	 * The getBestMove method is recursive. It first checks edge cases of when
	 * the game is over and when the player doesnt have a valid move. It returns
	 * specific values for these cases. It then checks the board for all
	 * possible moves and figures out which is the best by using board eval and
	 * updating the position of bestMove everytime it comes across a move with a
	 * higher value.
	 */
	public Move getBestMove(Tile[][] board, int intelligence, Color playerColor) {
		Move move = new Move();
		if (!_referee.playerHasValidMove(Color.BLACK, _board)
				&& !_referee.playerHasValidMove(Color.WHITE, _board)) {
			if (playerColor == Color.BLACK) {
				if (_game.getBlackPlayerScore() > _game.getWhitePlayerScore()) {
					move.setValue(9999999);
				}
				if (_game.getBlackPlayerScore() < _game.getWhitePlayerScore()) {
					move.setValue(-9999999);
				}
				if (_game.getBlackPlayerScore() == _game.getWhitePlayerScore()) {
					move.setValue(0);
				}
			}
			if (playerColor == Color.WHITE) {
				if (_game.getBlackPlayerScore() > _game.getWhitePlayerScore()) {
					move.setValue(-9999999);
				}
				if (_game.getBlackPlayerScore() < _game.getWhitePlayerScore()) {
					move.setValue(9999999);
				}
				if (_game.getBlackPlayerScore() == _game.getWhitePlayerScore()) {
					move.setValue(0);
				}
			}
		}
		if (!_referee.playerHasValidMove(playerColor, board)) {
			if (intelligence == 1) {
				move.setValue(-999999);
			} else {
				Move move2 = new Move();
				move2 = this.getBestMove(board, intelligence - 1,
						_referee.getOppositeColor(playerColor));
				if (-move2.getValue() > move.getValue()) {
					move.setRow(move2.getRow());
					move.setCol(move2.getCol());
					move.setValue(-move2.getValue());
				}
			}
		}
		for (int x = 0; x < 64; x++) {
			if (_referee.checkSandwich(playerColor, board, _moveList.get(x)
					.getRow(), _moveList.get(x).getCol())) {

				Tile[][] newBoard = _game.copyBoard(board);
				newBoard[_moveList.get(x).getRow()][_moveList.get(x).getCol()]
						.getPiece().setFill(playerColor);
				newBoard[_moveList.get(x).getRow()][_moveList.get(x).getCol()]
						.getPiece().setVisible(true);
				_referee.flipSandwiches(playerColor, newBoard, _moveList.get(x)
						.getRow(), _moveList.get(x).getCol());
				if (intelligence == 1) {
					if (this.boardEvaluation(newBoard, playerColor) > move
							.getValue()) {
						move.setRow(_moveList.get(x).getRow());
						move.setCol(_moveList.get(x).getCol());
						move.setValue(this.boardEvaluation(newBoard,
								playerColor));
					}
				} else {
					Move move2 = new Move();
					move2 = this.getBestMove(newBoard, intelligence - 1,
							_referee.getOppositeColor(playerColor));

					if (-move2.getValue() > move.getValue()) {
						move.setRow(_moveList.get(x).getRow());
						move.setCol(_moveList.get(x).getCol());
						move.setValue(-move2.getValue());
					}
				}
			}
		}
		return move;
	}

	/**
	 * Board eval uses the boardWeights in the game class to figure out how good
	 * of a board will result from a given move. It loops through the board and
	 * subtracts from the score for every opposing piece and adds for every
	 * piece of the players color.
	 */
	public int boardEvaluation(Tile[][] board, Color playerColor) {
		int boardScore = 0;
		for (int i = 1; i < 9; i++) {
			for (int j = 1; j < 9; j++) {
				if (board[i][j].getPiece().isVisible()) {
					if (board[i][j].getPiece().getFill() != playerColor) {
						boardScore = boardScore - _game.getBoardWeight(i, j);
					} else {
						boardScore = boardScore + _game.getBoardWeight(i, j);
					}
				}
			}
		}
		return boardScore;
	}

	/**
	 * The timehandler creates a pause in the computer players move so that the
	 * move can be seen. This pauses length is set by the slider when choosing
	 * player options.
	 */
	private class TimeHandler implements EventHandler<ActionEvent> {

		/**
		 * This is called by the start turn method if the computer player has a
		 * valid move.
		 */
		@Override
		public void handle(ActionEvent event) {
			_timeline.stop();
			move();
			event.consume();
		}
	}

}
