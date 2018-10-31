package GUI;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.regex.Pattern;

import core.Deck;
import core.Meld;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import observer.Game;

public class MainScreen extends Application {
	public Game table;
	public Player user;
	public Player p1;
	public Player p2;
	public Player p3;
	private Deck deck;
	
	private BorderPane canvas;
	private Scene scene;
	private HashMap<String, String> imageLoc;
	private HashMap<ImageView, Tile> associatedTiles;
	private TextArea consoleTextArea;
	private Button startButton;
	private Button endButton;
	private Button undoButton;
	//private Button sortButton;
	private Button playMeldButton;
	private VBox playMeldBox;
	private HBox userTilesBox;
	private HBox topCommandsBox;
	private TilePane playGrid;
	
	private ArrayList<ArrayList<Tile>> currentTurnMelds;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// Init things here.
		this.table = new Game();
		this.user = new Player(table, "User", new Strat0());
		this.p1 = new Player(table, "P1", new Strat1());
		this.p2 = new Player(table, "P2", new Strat2());
		this.p3 = new Player(table, "P3", new Strat3());
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
		
		//Set tiles for users using deck
		Player[] allPlayers = new Player[] {user, p1, p2, p3};
		
		for (Player player : allPlayers) {
			for (int c = 0; c < 14; c++) {
				player.addTile(deck.dealTile());
			}
		}
		
		// Init the user's temporary display meld array
		currentTurnMelds = new ArrayList<ArrayList<Tile>>();
		
		// temp
		ArrayList<Tile> m = new ArrayList<Tile>();
		m.add(new Tile("R", "6"));
		m.add(new Tile("R", "7"));
		m.add(new Tile("R", "8"));
		m.add(new Tile("R", "9"));
		m.add(new Tile("R", "10"));
		m.add(new Tile("R", "11"));
		m.add(new Tile("R", "12"));
		m.add(new Tile("R", "13"));
		/*m.add(new Tile("O", "3"));
		m.add(new Tile("O", "4"));
		m.add(new Tile("O", "5"));
		m.add(new Tile("O", "6"));
		m.add(new Tile("O", "7"));
		m.add(new Tile("O", "8"));
		m.add(new Tile("O", "9"));*/
		
		//ArrayList<Tile> m = new ArrayList<Tile>(m);
		//ArrayList<Tile> m2 = new ArrayList<Tile>(m);
		table.addMeldToTable(m);
		//table.addMeldToTable(m1);
		//table.addMeldToTable(m2);
	
		// move these 3 lines into the game loop
		associatedTiles.clear();		
		updateDisplayHand();
		updateDisplayTable();
		/* 
		 * while true
		 * 	foreach(player in allUsers)
		 * 		tiles = player.strat()
		 * 		table.addMeldsToTable(tiles)
		 * 		if player tiles == 0
		 * 			end game
		 * 		table.notifyObservers()  <- I'm not sure if this is done here or in the player class
		 * 		GUI.update()
		 * 	*/
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
			initGameElements();
		});
		
		// Set elements on the BorderPane areas
		canvas.setRight(consoleTextArea);
		canvas.setCenter(startButton);
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
		playGrid.setPadding(new Insets(10, 2, 10, 2));
		playGrid.setVgap(5);
		playGrid.setHgap(2);
		playGrid.setPrefRows(8);

		/*sort.addEventHandler(MouseEvent.MOUSE_CLICKED, ev -> {
			ObservableList<Node> uT = FXCollections.observableArrayList();
			uT = userTilesBox.getChildren();
			System.out.println(uT);
		});*/
		
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
		consoleTextArea.setMouseTransparent(true);
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
				currentTurnMelds.add(meldTiles);
				meldTiles.clear();
			}
			else {
				// Invalid meld
				displayToConsole("That meld is not valid.");
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
			displayToConsole("DEBUG: to end turn");
		});
		
		undoButton = new Button("Undo Turn");
		undoButton.addEventHandler(MouseEvent.MOUSE_CLICKED, ev -> {
			displayToConsole("This feature is unlockable via DLC.");
		});
		
		//sortButton = new Button ("Sort Hand");
		// Memento pattern. 
		undoButton.setDisable(true);
		// Remove the above button when undo is actually implemented.
		topCommandsBox.getChildren().addAll(endButton, undoButton);
	}

	private void displayToConsole(String s) {
		TextArea console = (TextArea) scene.lookup("#console");
		console.appendText(s + "\n");
	}
	
	private void updateDisplayTable() {
		clearDisplayTable();
		ArrayList<ArrayList<Tile>> tableMelds = (ArrayList<ArrayList<Tile>>) table.getTable();
		for (ArrayList<Tile> meld : tableMelds) {
			for (Tile meldTile : meld) {
				Image tile = new Image(new File(imageLoc.get(meldTile.toString())).toURI().toString());			
				DisplayTile dTile = new DisplayTile(tile, meldTile);
				dTile.iv.addEventHandler(MouseEvent.MOUSE_CLICKED, ev -> {
					if (dTile.isOrigin) {
						dTile.isOrigin = !dTile.isOrigin;
						dTile.lastIndex = playGrid.getChildren().indexOf(ev.getSource());
						playGrid.getChildren().set(dTile.lastIndex, new Region());
						playMeldBox.getChildren().add(dTile.iv);
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
		// Also loop through the "temporary melds" as mentioned below in the playMeld's click handler
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
				}
				else {
					// Not in original spot, move it back there
					//playMeldBox.getChildren().remove(dTile.iv);
					userTilesBox.getChildren().set(dTile.lastIndex, dTile.iv);
					dTile.isOrigin = !dTile.isOrigin;
				}
			});
			userTilesBox.getChildren().addAll(dTile.iv);
			associatedTiles.put(dTile.iv, userTile);
		}
	}
	
	private void clearDisplayHand() {
		userTilesBox.getChildren().clear();
	}
}
