package Othello;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * This is the game class! The game class has method for updating the game
 * logic. It keeps track of the score and displays which players turn it is.
 */
public class Game {

	private Pane _boardPane;
	private VBox _settingsPane;
	private Tile[][] _board;
	private Referee _referee;
	private Player _blackPlayer;
	private Player _whitePlayer;
	private Game _game;
	private int _blackPlayerScore;
	private int _whitePlayerScore;
	private Label _scoreLabelBlack;
	private Label _scoreLabelWhite;
	private Label _instructionLabel;
	private Button _applySettingsButton;
	private int[][] _boardWeights;
	private double _sliderVariable;
	private Label _computerSpeedLabel;
	private Slider _slider;
	private PaneOrganizer _paneOrganizer;

	/**
	 * The constructor makes the board, the board weights that are used by the
	 * computer player, and the settings pane. The settings pane is long but I
	 * found it more simple to just keep it all in one place instead of
	 * distributed in another control class.
	 */
	public Game(PaneOrganizer paneOrganizer) {
		_game = this;
		this.makeBoard();
		this.makeBoardWeights();
		this.makeSettings();
		_referee = new Referee(this);
		_paneOrganizer = paneOrganizer;
	}

	/**
	 * Update score counts all of the white pieces and all of the black pieces
	 * and then sets the int instance variables equal to them.
	 */
	public void updateScore() {
		int scoreBlack = 0;
		int scoreWhite = 0;
		for (int row = 1; row < 9; row++) {
			for (int col = 1; col < 9; col++) {
				if (_board[row][col].getPiece().isVisible()) {
					if (_board[row][col].getPiece().getFill() == Color.BLACK) {
						scoreBlack++;
					}
				}
				if (_board[row][col].getPiece().getFill() == Color.WHITE) {
					scoreWhite++;
				}
			}
		}
		_blackPlayerScore = scoreBlack;
		_whitePlayerScore = scoreWhite;
		_scoreLabelBlack.setText("Black: " + _blackPlayerScore);
		_scoreLabelWhite.setText("White: " + _whitePlayerScore);
	}

	/**
	 * Updates the instruction label after each turn. Instruction label also
	 * shows directions at start and Game Over at end.
	 */
	public void updateInstructionLabel(Color playerColor) {
		if (playerColor == Color.BLACK) {
			_instructionLabel.setText("Current Turn: Black Player");
		} else if (playerColor == Color.WHITE) {
			_instructionLabel.setText("Current Turn: White Player");
		}
	}

	/**
	 * Called by Referee if neither player can make a move.
	 */
	public void endGame() {
		if (_blackPlayerScore > _whitePlayerScore) {
			_game.getInstructionLabel().setText("GAME OVER! Black Player Wins");

		}
		if (_whitePlayerScore > _blackPlayerScore) {
			_game.getInstructionLabel().setText("GAME OVER! White Player Wins");

		}
		if (_whitePlayerScore == _blackPlayerScore) {
			_game.getInstructionLabel().setText("GAME OVER! It is a tie!");
		}
	}

	/**
	 * Called by constructor to lay out the graphical/logical display of tiles.
	 * 4 pieces set in the center.
	 */
	private void makeBoard() {
		_boardPane = new Pane();
		_boardPane.setFocusTraversable(true);
		_boardPane.setPrefSize(Constants.SQUARE_SIZE * 10,
				Constants.SQUARE_SIZE * 10);
		_boardPane.setStyle("-fx-background-color: pink;");
		_board = new Tile[10][10];
		for (int row = 1; row < 9; row++) {
			for (int col = 1; col < 9; col++) {
				_board[row][col] = new Tile();
				_board[row][col].setLocation(col * Constants.SQUARE_SIZE, row
						* Constants.SQUARE_SIZE);
				_boardPane.getChildren().addAll(_board[row][col].getTile(),
						_board[row][col].getPiece());
			}
		}
		_board[4][4].getPiece().setFill(Color.BLACK);
		_board[4][4].getPiece().setVisible(true);
		_board[5][4].getPiece().setFill(Color.WHITE);
		_board[5][4].getPiece().setVisible(true);
		_board[4][5].getPiece().setFill(Color.WHITE);
		_board[4][5].getPiece().setVisible(true);
		_board[5][5].getPiece().setFill(Color.BLACK);
		_board[5][5].getPiece().setVisible(true);
	}

	/**
	 * Very important method! Used by computer player to test out moves. Because
	 * I made my board an array of Tiles, this made more sense to make than a
	 * traditional copy constructor.
	 */
	public Tile[][] copyBoard(Tile[][] oldBoard) {
		Tile[][] newBoard = new Tile[10][10];
		for (int row = 1; row < 9; row++) {
			for (int col = 1; col < 9; col++) {
				newBoard[row][col] = new Tile();
				newBoard[row][col].getPiece().setFill(
						oldBoard[row][col].getPiece().getFill());
				newBoard[row][col].getPiece().setVisible(
						oldBoard[row][col].getPiece().isVisible());
			}
		}
		return newBoard;
	}

	/**
	 * This just makes the board weights the computer player uses to evaluate
	 * the board and determine which plays are most beneficial.
	 */
	private void makeBoardWeights() {
		_boardWeights = new int[][] { { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 200, -70, 30, 25, 25, 30, -70, 200, 0 },
				{ 0, -70, -100, -10, -10, -10, -10, -100, -70, 0 },
				{ 0, 30, 2323, 2, 2, 2, 2, -10, 30, 0 },
				{ 0, 25, -10, 2, 2, 2, 2, -10, 25, 0 },
				{ 0, 25, -10, 2, 2, 2, 2, -10, 25, 0 },
				{ 0, 30, -10, 2, 2, 2, 2, -10, 30, 0 },
				{ 0, -70, -100, -10, -10, -10, -10, -100, -70, 0 },
				{ 0, 200, -70, 30, 25, 25, 30, -70, 200, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };
	}

	/**
	 * These accessors are used by other classes to access the variables created
	 * in this class. The board weights, the panes, the ref, etc.
	 */
	public int getBoardWeight(int x, int y) {
		return _boardWeights[x][y];
	}

	public double getSliderVariable() {
		return _sliderVariable;
	}

	public Player getBlackPlayer() {
		return _blackPlayer;
	}

	public Player getWhitePlayer() {
		return _whitePlayer;
	}

	public int getBlackPlayerScore() {
		return _blackPlayerScore;
	}

	public int getWhitePlayerScore() {
		return _whitePlayerScore;
	}

	public Referee getReferee() {
		return _referee;
	}

	public Tile[][] getBoard() {
		return _board;
	}

	public Pane getBoardPane() {
		return _boardPane;
	}

	public VBox getSettingsPane() {
		return _settingsPane;
	}

	public Label getInstructionLabel() {
		return _instructionLabel;
	}

	/**
	 * This method is long but I found having a control class to be way more
	 * confusing logically. This just creates all of the objects and logic of
	 * the settingsPane. All the buttons for selecting players and
	 * starting/resetting the game etc are made here.
	 */
	private void makeSettings() {
		_settingsPane = new VBox(30);
		_settingsPane.setPrefSize(Constants.SQUARE_SIZE * 4,
				Constants.SQUARE_SIZE * 10);
		_settingsPane.setStyle("-fx-background-color: pink;");
		_settingsPane.setAlignment(Pos.CENTER);

		_instructionLabel = new Label(
				"Select options, then press apply settings.");

		HBox scorePane = new HBox(30);
		scorePane.setAlignment(Pos.CENTER);
		_scoreLabelBlack = new Label("Black Player");
		_scoreLabelWhite = new Label("White Player");
		scorePane.getChildren().addAll(_scoreLabelBlack, _scoreLabelWhite);

		HBox playerOptionsPane = new HBox(30);
		playerOptionsPane.setAlignment(Pos.CENTER);
		VBox playerOptionsPaneBlack = new VBox(30);
		VBox playerOptionsPaneWhite = new VBox(30);
		playerOptionsPaneBlack.setAlignment(Pos.CENTER);
		playerOptionsPaneWhite.setAlignment(Pos.CENTER);

		_slider = new Slider(0, 100, 50);
		_sliderVariable = 10.0;
		_slider.valueProperty()
				.addListener(new SliderListener(_sliderVariable));
		_computerSpeedLabel = new Label("Computer Player Speed: "
				+ (int) _game.getSliderVariable());
		_slider.setVisible(false);
		_computerSpeedLabel.setVisible(false);

		// Creates buttons and checkbox for white player and sets up their logic
		ToggleGroup optionsBlack = new ToggleGroup();
		RadioButton button1b = new RadioButton("Human");
		RadioButton button2b = new RadioButton("Computer 1");
		RadioButton button3b = new RadioButton("Computer 2");
		RadioButton button4b = new RadioButton("Computer 3");
		CheckBox nonDeterministicBlack = new CheckBox("Nondeterministic");
		nonDeterministicBlack.setVisible(false);
		button1b.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				nonDeterministicBlack.setVisible(false);
				_computerSpeedLabel.setVisible(false);
				_slider.setVisible(false);
				event.consume();
			}
		});
		button2b.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				nonDeterministicBlack.setVisible(true);
				_computerSpeedLabel.setVisible(true);
				_slider.setVisible(true);
				event.consume();
			}
		});
		button3b.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				nonDeterministicBlack.setVisible(true);
				_computerSpeedLabel.setVisible(true);
				_slider.setVisible(true);
				event.consume();
			}
		});
		button4b.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				nonDeterministicBlack.setVisible(true);
				_computerSpeedLabel.setVisible(true);
				_slider.setVisible(true);
				event.consume();
			}
		});
		// Creates buttons and checkbox for white player and sets up their logic
		button1b.setToggleGroup(optionsBlack);
		button2b.setToggleGroup(optionsBlack);
		button3b.setToggleGroup(optionsBlack);
		button4b.setToggleGroup(optionsBlack);
		button1b.setSelected(true);
		ToggleGroup optionsWhite = new ToggleGroup();
		RadioButton button1w = new RadioButton("Human");
		RadioButton button2w = new RadioButton("Computer 1");
		RadioButton button3w = new RadioButton("Computer 2");
		RadioButton button4w = new RadioButton("Computer 3");
		CheckBox nonDeterministicWhite = new CheckBox("Nondeterministic");
		nonDeterministicWhite.setVisible(false);
		button1w.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				nonDeterministicWhite.setVisible(false);
				_computerSpeedLabel.setVisible(false);
				_slider.setVisible(false);
				event.consume();
			}
		});
		button2w.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				nonDeterministicWhite.setVisible(true);
				_computerSpeedLabel.setVisible(true);
				_slider.setVisible(true);
				event.consume();
			}
		});
		button3w.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				nonDeterministicWhite.setVisible(true);
				_computerSpeedLabel.setVisible(true);
				_slider.setVisible(true);
				event.consume();
			}
		});
		button4w.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				nonDeterministicWhite.setVisible(true);
				_computerSpeedLabel.setVisible(true);
				_slider.setVisible(true);
				event.consume();
			}
		});
		button1w.setToggleGroup(optionsWhite);
		button2w.setToggleGroup(optionsWhite);
		button3w.setToggleGroup(optionsWhite);
		button4w.setToggleGroup(optionsWhite);
		button1w.setSelected(true);
		playerOptionsPaneBlack.getChildren().addAll(button1b, button2b,
				button3b, button4b, nonDeterministicBlack);
		playerOptionsPaneWhite.getChildren().addAll(button1w, button2w,
				button3w, button4w, nonDeterministicWhite);
		playerOptionsPane.getChildren().addAll(playerOptionsPaneBlack,
				playerOptionsPaneWhite);

		_applySettingsButton = new Button("Apply Settings");
		_applySettingsButton.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (button1b.isSelected()) {
					_blackPlayer = new HumanPlayer(_game, Color.BLACK);
				} else if (button2b.isSelected()) {
					_blackPlayer = new ComputerPlayer(_game, Color.BLACK, 1,
							nonDeterministicBlack.isSelected());
				} else if (button3b.isSelected()) {
					_blackPlayer = new ComputerPlayer(_game, Color.BLACK, 2,
							nonDeterministicBlack.isSelected());
				} else if (button4b.isSelected()) {
					_blackPlayer = new ComputerPlayer(_game, Color.BLACK, 3,
							nonDeterministicBlack.isSelected());
				}
				if (button1w.isSelected()) {
					_whitePlayer = new HumanPlayer(_game, Color.WHITE);
				} else if (button2w.isSelected()) {
					_whitePlayer = new ComputerPlayer(_game, Color.WHITE, 1,
							nonDeterministicWhite.isSelected());
				} else if (button3w.isSelected()) {
					_whitePlayer = new ComputerPlayer(_game, Color.WHITE, 2,
							nonDeterministicWhite.isSelected());
				} else if (button4w.isSelected()) {
					_whitePlayer = new ComputerPlayer(_game, Color.WHITE, 3,
							nonDeterministicWhite.isSelected());
				}
				_referee.newGame();
				updateInstructionLabel(Color.BLACK);
				_applySettingsButton.setVisible(false);
				_slider.setDisable(true);
				event.consume();
			}
		});

		Button resetButton = new Button("Reset");
		resetButton.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				_paneOrganizer.resetGame();
				event.consume();
			}
		});

		Button quitButton = new Button("Quit");
		quitButton.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Platform.exit();
				event.consume();
			}
		});

		_settingsPane.getChildren().addAll(_instructionLabel, scorePane,
				playerOptionsPane, _slider, _computerSpeedLabel,
				_applySettingsButton, resetButton, quitButton);
	}

	/**
	 * This Slide listener retrieves where the slider is set to and gives the
	 * corresponding value as an argument to the changed method. This value is
	 * used to change the time the computer waits before executing its move,
	 * this is set when the apply settings button is pressed.
	 */
	private class SliderListener implements ChangeListener<Number> {

		// Constructor does not do anything.
		public SliderListener(Double _sliderVariable) {
		}

		/**
		 * This method gives the value of of the corresponding position of the
		 * sliders knob when it is moved around. This updates the keyframe the
		 * is set when apply settings is pressed.
		 */
		public void changed(ObservableValue<? extends Number> observable,
				Number oldValue, Number newValue) {
			_sliderVariable = 0.2 * (newValue.doubleValue());
			_computerSpeedLabel.setText("Computer Player Speed: "
					+ (int) _game.getSliderVariable());
		}
	}
}
