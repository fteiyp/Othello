package Othello;

import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;

/**
 * The tile class is a class for store the square and the ellipse that make up
 * each part of the board. The squares are only changed by the human player to
 * mark viable moves. The pieces are accessed a lot for making moves and
 * checking moves.
 */
public class Tile {

	private Ellipse _piece;
	private Rectangle _tile;

	/**
	 * The constructor sets the default properties of the square and ellipse.
	 */
	public Tile() {
		_tile = new Rectangle();
		_tile.setFill(Color.GREEN);
		_tile.setStroke(Color.BLACK);
		_tile.setHeight(Constants.SQUARE_SIZE);
		_tile.setWidth(Constants.SQUARE_SIZE);
		_piece = new Ellipse();
		_piece.setStroke(Color.BLACK);
		_piece.setRadiusX(Constants.SQUARE_SIZE * .45);
		_piece.setRadiusY(Constants.SQUARE_SIZE * .45);
		_piece.setVisible(false);
	}

	/**
	 * This sets the pieces location - its used when the game makes the board.
	 */
	public void setLocation(int x, int y) {
		_tile.setX(x);
		_tile.setY(y);
		_piece.setCenterX(Constants.SQUARE_SIZE * .5 + x);
		_piece.setCenterY(Constants.SQUARE_SIZE * .5 + y);
	}

	/**
	 * These getters are used throughout the game for changing the states of the
	 * pieces i.e. making moves, flipping pieces, etc.
	 */
	public Ellipse getPiece() {
		return _piece;
	}

	public Rectangle getTile() {
		return _tile;
	}

}
