package Othello;

/**
 * The Move class is simply a way to store the row and column of different
 * possible moves. It also stores a value associated with that move for the
 * minimax algorithm to update and use.
 */
public class Move {
	private int _row;
	private int _col;
	private int _value;

	/**
	 * The constructor sets the default values. The value is initially low so
	 * that the mini max algorithm always updates its moves.
	 */
	Move() {
		_row = 0;
		_col = 0;
		_value = -999999999;
	}

	/**
	 * These setters and getters are used for storing and updating the row,
	 * column, and value of the different moves.
	 */
	public int getRow() {
		return _row;
	}

	public int getCol() {
		return _col;
	}

	public int getValue() {
		return _value;
	}

	public void setRow(int row) {
		_row = row;
	}

	public void setCol(int col) {
		_col = col;
	}

	public void setValue(int value) {
		_value = value;
	}

}
