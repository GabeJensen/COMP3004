package GUI;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import core.Caretaker;
import core.Deck;
import core.Meld;
import core.Originator;
import core.Player;
import core.Strat0;
import core.Strat1;
import core.Strat2;
import core.Strat3;
import core.Tile;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import observer.Game;

public class MainScreen extends Application {
	public Game game;
	public Player user;
	public Player p1;
	public Player p2;
	public Player p3;
	private Deck deck;
	private Originator originator;
	private Caretaker caretaker;
	private static MediaPlayer m;
	private BorderPane canvas;
	private Scene scene;
	private HashMap<String, String> imageLoc;
	private HashMap<ImageView, Tile> associatedTiles;
	private TextArea consoleTextArea;
	private Button startButton;
	private Button endButton;
	private Button undoButton;
	private Button playMeldButton;
	private VBox playMeldBox;
	private HBox userTilesBox;
	private HBox topCommandsBox;
	private TilePane playGrid;
	private ArrayList<ArrayList<Tile>> currentTurnMelds;
	private ArrayList<Tile> currentTurnUserUsedTiles;
	private final InnerShadow handPlayEffect = new InnerShadow(20, Color.RED);
	private final InnerShadow tablePlayEffect = new InnerShadow(20, Color.BLUE);
	private ArrayList<Tile> tablePlayTiles;
	private boolean emptyDeck;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// Init things here.
		this.game = new Game();
		this.originator = new Originator();
		this.caretaker = new Caretaker();
		this.user = new Player(game, "User", new Strat0());
		this.p1 = new Player(game, "P1", new Strat1());
		this.p2 = new Player(game, "P2", new Strat2());
		this.p3 = new Player(game, "P3", new Strat3());
		game.setPlayers(user, p1, p2, p3);
		initWindow(primaryStage);
	}

	public void playGame() {
		//Create and initialize GUI
		/* Create buttons for:
		 * - Starting the game
		 * - Reseting table to start of turn
		 * - Ending your turn (checking melds)
		 * */
		
		//Create deck
		deck = new Deck();
		deck.shuffleDeck();
		emptyDeck = false;
		
		//Set tiles for users using deck
		Player[] allPlayers = {user, p1, p2, p3};
		
		for (Player player : allPlayers) {
			for (int c = 0; c < 14; c++) {
				player.addTile(deck.dealTile());
			}
		}
		
		// Init the user's temporary display meld array
		currentTurnMelds = new ArrayList<ArrayList<Tile>>();
		currentTurnUserUsedTiles = new ArrayList<Tile>();
		tablePlayTiles = new ArrayList<Tile>();
		
		updateDisplayHand();
		updateDisplayTable();
		
		// Start of the game. We still want to be able to revert here.
		originator.setState(game.getTable(), user.getTiles());
		caretaker.set(originator.saveMemento());
	}
	
	private void gameLoop() {
		Player[] nonHumanPlayers = {p1, p2, p3};
		int turnValue;
		Tile tile;
		for (Player p : nonHumanPlayers) {
			turnValue = p.performStrategy();
			if (turnValue == 0) {
				if(!emptyDeck) {
					tile = deck.dealTile();
					if(tile == null) {
						emptyDeck = true;
						displayToConsole(p.getName() + " tried drawing, but the deck was empty!");
					} else {
						p.addTile(tile);											
					}
				}
				continue;
			}
			else if (turnValue == 1) {
				displayToConsole(p.getName() + " played this turn.");
				if(p.getHandCount() == 0) {
					displayToConsole(p.getName() + " says: 'RUMMIKUB!' They won the game!");
					disableButtons();
					break;
				}
				continue;
			}
		}
		associatedTiles.clear();		
		updateDisplayHand();
		updateDisplayTable();
		originator.setState(game.getTable(), user.getTiles());
		caretaker.set(originator.saveMemento());
	}

	private void initWindow(Stage primaryStage) {
		BorderPane canvas = new BorderPane();
		this.canvas = canvas;
		canvas.setId("canvas");
		
		initCanvasElements();
		
		Scene scene = new Scene(canvas, 1450, 900);
		scene.getStylesheets().addAll(this.getClass().getResource("./style.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.setTitle("Team 8 - Tile Rummy");

		this.scene = scene;
		
		primaryStage.show();
	}
	
	private void initCanvasElements() {
		// Console (side text are)
		initConsole();
		
		// Preload the tile info
		loadTileImages();
		
		// Start Button
		startButton = new Button("Start Game");
		//startButton.relocate(1155/2, 900/2);
		
		startButton.addEventHandler(MouseEvent.MOUSE_CLICKED, ev -> {
			displayToConsole("Started a game of Tile Rummy!");
			canvas.setCenter(null);
			music();
			initGameElements();
		});
		
		// Set elements on the BorderPane areas
		canvas.setRight(consoleTextArea);
		canvas.setCenter(startButton);
	}
	
	private void music() {
		Media media = new Media(Paths.get("src/main/resources/b.mp3").toUri().toString());
		m = new MediaPlayer(media);
		m.setCycleCount(MediaPlayer.INDEFINITE);
		m.play();
	}

	private void initGameElements() {
		// Bottom card section
		userTilesBox = new HBox();
		userTilesBox.setPadding(new Insets(5,5,0,5));
		userTilesBox.setSpacing(10);
		
		// Commands at top
		initTopCommands();
		
		// Left borderPane column for melds
		initMeldArea();
		
		// Grid in middle
		playGrid = new TilePane();
		playGrid.setPadding(new Insets(5, 2, 10, 4));
		playGrid.setVgap(9);
		playGrid.setHgap(6);
		playGrid.setPrefRows(8);
		playGrid.getStyleClass().addAll("pane");
		canvas.setMargin(playGrid, new Insets(0, 28, 8, 0));
		
		playMeldBox.setPrefWidth(350);
		
		canvas.setCenter(playGrid);
		canvas.setTop(topCommandsBox);
		canvas.setBottom(userTilesBox);
		canvas.setLeft(playMeldBox);
		//userTilesBox.getChildren().addAll();
		
		playGame();
	}

	private void initConsole() {
		consoleTextArea = new TextArea();
		consoleTextArea.setPrefHeight(900);
		consoleTextArea.setPrefWidth(300);
		//console.relocate(1155,5);
		consoleTextArea.setEditable(false);
		consoleTextArea.setFocusTraversable(false);
		consoleTextArea.setWrapText(true);
		consoleTextArea.setId("console");
		
		consoleTextArea.textProperty().addListener((element, oldValue, newValue) -> {
			// Auto scroll to bottom when appending text to console pane
			consoleTextArea.setScrollTop(Double.MAX_VALUE);
		});
	}

	private void loadTileImages() {
		String tileResources = "src/main/resources/";
		File tileLoc = new File(tileResources);
		File[] tileImages = tileLoc.listFiles();
		imageLoc = new HashMap<String, String>();
		associatedTiles = new HashMap<ImageView, Tile>();
		for (int i = 0; i < tileImages.length; i++) {
			String fileName = tileImages[i].getName();
			String[] imgInf = fileName.split(Pattern.quote("."));
			imageLoc.put(imgInf[0], tileResources + tileImages[i].getName());
		}
	}

	private void initMeldArea() {
		playMeldBox = new VBox();
		playMeldBox.setPadding(new Insets(20,0,0,25));
		playMeldButton = new Button("   Play Meld   ");
		playMeldButton.addEventHandler(MouseEvent.MOUSE_CLICKED, ev -> {
			ArrayList<Tile> meldTiles = new ArrayList<Tile>();
			// Start from 1, since that's always the Play Meld button.
			for (int c = 1; c < playMeldBox.getChildren().size(); c++) {
				meldTiles.add(associatedTiles.get(playMeldBox.getChildren().get(c)));
			}
			if (Meld.checkValidity(meldTiles)) {
				// Valid meld. Accept and put into the currentTurnMelds
				currentTurnMelds.add(meldTiles);
				playMeldBox.getChildren().remove(1, playMeldBox.getChildren().size());
				updateTempMelds();
			}
			else {
				// Invalid meld
				displayToConsole("That meld is not valid!");
			}
			
		});
		
		playMeldBox.getChildren().addAll(playMeldButton);
	}

	private void initTopCommands() {
		topCommandsBox = new HBox();
		topCommandsBox.setPadding(new Insets(5,10,0,10));
		topCommandsBox.setSpacing(10);
		
		endButton = new Button("End Turn");
		endButton.addEventHandler(MouseEvent.MOUSE_CLICKED, ev -> {
			
			// If the user didn't play anything this turn.
			if ((currentTurnMelds.isEmpty()) && currentTurnUserUsedTiles.isEmpty()) {
				if(!emptyDeck) {
					Tile draw = deck.dealTile();
					if(draw == null) {
						emptyDeck = true;
						displayToConsole(user.getName() + " tried drawing, but the deck was empty!");
					} else {
						displayToConsole("User draws " + draw.toString() + "!");
						user.addTile(draw);						
					}
				} else {
					displayToConsole(user.getName() + " tried drawing, but the deck was empty!");
				}
			}
			else {
				// If the user has not played their initial 30 meld yet
				if (!user.getInit30Flag()){
					// No table tiles allowed to be played until the initial 30 meld(s)
					int tileCount = 0;
					for (ArrayList<Tile> meld : currentTurnMelds) {
						for (@SuppressWarnings("unused") Tile t : meld) {
							tileCount++;
						}
					}
					// If the tile count of hand melds is the same as the count of total tiles used, it means that only hand melds were played
					if (tileCount == currentTurnUserUsedTiles.size()) {
						// User has to play HAND melds that add up to 30 or more and nothing else
						int handMeldSum = 0;
						for (ArrayList<Tile> meld : currentTurnMelds) {
							handMeldSum += Meld.getValue(meld);
						}
						if (handMeldSum < 30) {
							displayToConsole("The value of the hand melds played do not add up to at least 30 points!");
							return;
						}
						else {
							user.playedInit30();
						}
					}
					else {
						displayToConsole("You must play the the initial greater than 30 valued hand meld(s) first!");
						return;
					}
				}
				//else if (user.getInit30Flag()) {
					// They have played their 30 meld. It is a regular turn from then on.
				//}
				if (checkPlayGrid()) {
					List<ArrayList<Tile>> newTable = new ArrayList<ArrayList<Tile>>();
					
					//generate the new table for Game
					ArrayList<Tile> meld = new ArrayList<Tile>();
					for (int i = 0; i < playGrid.getChildren().size(); ++i) {
						//hit blank space, add to newTable if size != 0
						if (playGrid.getChildren().get(i) instanceof Region) {
							if (meld.size() == 0) {
								continue;
							} else {
								newTable.add(meld);
								meld = new ArrayList<Tile>();
							}
						}else if (playGrid.getChildren().get(i) instanceof ImageView) {
							//add tile to meld
							ImageView tileIV = (ImageView) playGrid.getChildren().get(i);
							meld.add(associatedTiles.get(tileIV));
						}
					}
									
					//remove the user used tiles from user hand
					for (int i = 0; i < currentTurnUserUsedTiles.size(); ++i) {
						user.removeTile(currentTurnUserUsedTiles.get(i));
					}
					game.setTable(newTable);
				} 
				else {
					displayToConsole("The table has some invalid melds!");
					return;
				}
			}
			
			currentTurnMelds.clear();
			currentTurnUserUsedTiles.clear();
			if(user.getHandCount() == 0) {
				displayToConsole(user.getName() + " says: 'RUMMIKUB!' They won the game!");
				disableButtons();
			} else {
				gameLoop();				
			}
		});
		
		undoButton = new Button("Undo Turn");
		undoButton.addEventHandler(MouseEvent.MOUSE_CLICKED, ev -> {
			originator.restoreMemento(caretaker.get());
			
			game.setTable(originator.getState().getTable());
			user.setTiles(originator.getState().getHand());
			
			playMeldBox.getChildren().remove(1, playMeldBox.getChildren().size());
			
			currentTurnMelds = new ArrayList<ArrayList<Tile>>();
			currentTurnUserUsedTiles = new ArrayList<Tile>();
			
			updateDisplayHand();
			updateDisplayTable();
		});
		
		topCommandsBox.getChildren().addAll(endButton, undoButton);
	}

	private void displayToConsole(String s) {
		consoleTextArea.appendText(s + "\n");
	}
	
	private void updateTempMelds() {
		for (int x = currentTurnMelds.size() - 1; x < currentTurnMelds.size(); x++) {
			for (Tile meldTile : currentTurnMelds.get(x)) {
				Image tile = new Image(new File(imageLoc.get(meldTile.toString())).toURI().toString());			
				DisplayTile dTile = new DisplayTile(tile, meldTile);
				if(tablePlayTiles.contains(dTile.tile)) {
					dTile.iv.setEffect(tablePlayEffect);
				} else {
					dTile.iv.setEffect(handPlayEffect);					
				}
//				dTile.iv.addEventHandler(MouseEvent.MOUSE_CLICKED, ev -> {
//					if (dTile.isOrigin) {
//						dTile.isOrigin = !dTile.isOrigin;
//						dTile.lastIndex = playGrid.getChildren().indexOf(ev.getSource());
//						playGrid.getChildren().set(dTile.lastIndex, new Region());
//						playMeldBox.getChildren().add(dTile.iv);
//					}
//					else {
//						playGrid.getChildren().set(dTile.lastIndex, dTile.iv);
//						dTile.isOrigin = !dTile.isOrigin;
//					}
//				});
				playGrid.getChildren().add(dTile.iv);
				associatedTiles.put(dTile.iv, meldTile);
			}
			playGrid.getChildren().add(new Region());
		}
		tablePlayTiles.clear();
	}
	
	private void updateDisplayTable() {
		clearDisplayTable();
		ArrayList<ArrayList<Tile>> tableMelds = (ArrayList<ArrayList<Tile>>) game.getTable();
		for (ArrayList<Tile> meld : tableMelds) {
			for (Tile meldTile : meld) {
				Image tile = new Image(new File(imageLoc.get(meldTile.toString())).toURI().toString());			
				DisplayTile dTile = new DisplayTile(tile, meldTile);
//				dTile.iv.setEffect(handPlayEffect);
				dTile.iv.addEventHandler(MouseEvent.MOUSE_CLICKED, ev -> {
					if (dTile.isOrigin) {
						dTile.isOrigin = !dTile.isOrigin;
						dTile.lastIndex = playGrid.getChildren().indexOf(ev.getSource());
						playGrid.getChildren().set(dTile.lastIndex, new Region());
						playMeldBox.getChildren().add(dTile.iv);
						tablePlayTiles.add(dTile.tile);
					}
					else {
						playGrid.getChildren().set(dTile.lastIndex, dTile.iv);
						dTile.isOrigin = !dTile.isOrigin;
					}
				});
				playGrid.getChildren().add(dTile.iv);
				associatedTiles.put(dTile.iv, meldTile);
			}
			playGrid.getChildren().add(new Region());
		}
	}
	
	private void clearDisplayTable () {
		playGrid.getChildren().clear();
	}
	
	private void updateDisplayHand() {
		clearDisplayHand();
		ArrayList<Tile> userHand = user.getTiles();
		for (Tile userTile : userHand) {
			Image tile = new Image(new File(imageLoc.get(userTile.toString())).toURI().toString());			
			DisplayTile dTile = new DisplayTile(tile, userTile);
			dTile.iv.addEventHandler(MouseEvent.MOUSE_CLICKED, ev -> {
				if (dTile.isOrigin) {
					// In original spot
					dTile.lastIndex = userTilesBox.getChildren().indexOf(ev.getSource());
					//dTile.setOriginCoordinates(ev.getSceneX(), ev.getSceneY());
					dTile.isOrigin = !dTile.isOrigin;
					userTilesBox.getChildren().set(dTile.lastIndex, new Region());
					playMeldBox.getChildren().add(dTile.iv);
					currentTurnUserUsedTiles.add(dTile.tile);
				}
				else {
					// Not in original spot, move it back there
					//playMeldBox.getChildren().remove(dTile.iv);
					userTilesBox.getChildren().set(dTile.lastIndex, dTile.iv);
					dTile.isOrigin = !dTile.isOrigin;
					currentTurnUserUsedTiles.remove(dTile.tile);
				}
			});
			userTilesBox.getChildren().addAll(dTile.iv);
			associatedTiles.put(dTile.iv, userTile);
		}
	}
	
	private void clearDisplayHand() {
		userTilesBox.getChildren().clear();
	}
	
	private boolean checkPlayGrid() {
		ArrayList<Tile> checkMeld = new ArrayList<Tile>();
		for (int i = 0; i < playGrid.getChildren().size(); ++i) {
			//hit a blank space
			if (playGrid.getChildren().get(i) instanceof Region) {
				//haven't added tiles to checkMeld or valid meld
				if (checkMeld.size() == 0 || Meld.checkValidity(checkMeld)) {
					checkMeld.clear();
					continue;
				} else {
					//invalid meld
					return false;
				}
			}else if (playGrid.getChildren().get(i) instanceof ImageView) {
				//add tile to checkMeld
				ImageView tileIV = (ImageView) playGrid.getChildren().get(i);
				checkMeld.add(associatedTiles.get(tileIV));
			}
		}
		
		//play grid all good
		return true;
	}
	
	private void disableButtons() {
		endButton.setDisable(true);
		playMeldButton.setDisable(true);
		undoButton.setDisable(true);
	}
}
