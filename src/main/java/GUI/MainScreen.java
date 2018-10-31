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
	private TextArea console;
	private Button start;
	private Button end;
	private Button undo;
	//private Button sort;
	private Button playMeld;
	private VBox meldArea;
	private HBox userTiles;
	private HBox topCommands;
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
	
	private void displayToConsole(String s) {
		TextArea console = (TextArea) scene.lookup("#console");
		console.appendText(s + "\n");
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
						meldArea.getChildren().add(dTile.iv);
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
					dTile.lastIndex = userTiles.getChildren().indexOf(ev.getSource());
					//dTile.setOriginCoordinates(ev.getSceneX(), ev.getSceneY());
					dTile.isOrigin = !dTile.isOrigin;
					userTiles.getChildren().set(dTile.lastIndex, new Region());
					meldArea.getChildren().add(dTile.iv);
				}
				else {
					// Not in original spot, move it back there
					//meldArea.getChildren().remove(dTile.iv);
					userTiles.getChildren().set(dTile.lastIndex, dTile.iv);
					dTile.isOrigin = !dTile.isOrigin;
				}
			});
			userTiles.getChildren().addAll(dTile.iv);
			associatedTiles.put(dTile.iv, userTile);
		}
	}
	
	private void clearDisplayHand() {
		userTiles.getChildren().clear();
	}
	
	private void initGameElements() {
		// Bottom card section
		userTiles = new HBox();
		userTiles.setPadding(new Insets(5,5,0,5));
		userTiles.setSpacing(10);
		
		// Commands at top
		topCommands = new HBox();
		topCommands.setPadding(new Insets(5,10,0,10));
		topCommands.setSpacing(10);
		end = new Button("End Turn");
		undo = new Button("Undo Turn");
		//sort = new Button ("Sort Hand");
		// Memento pattern. 
		undo.setDisable(true);
		// Remove the above button when undo is actually implemented.
		topCommands.getChildren().addAll(end, undo);
		
		// Left borderPane column for melds
		meldArea = new VBox();
		meldArea.setPadding(new Insets(20,0,0,25));
		playMeld = new Button("   Play Meld   ");
		playMeld.addEventHandler(MouseEvent.MOUSE_CLICKED, ev -> {
			ArrayList<Tile> meldTiles = new ArrayList<Tile>();
			// Start from 1, since that's always the Play Meld button.
			for (int c = 1; c < meldArea.getChildren().size(); c++) {
				meldTiles.add(associatedTiles.get(meldArea.getChildren().get(c)));
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
		
		meldArea.getChildren().addAll(playMeld);
		
		// Grid in middle
		playGrid = new TilePane();
		playGrid.setPadding(new Insets(10, 2, 10, 2));
		playGrid.setVgap(5);
		playGrid.setHgap(2);
		playGrid.setPrefRows(8);
		
		end.addEventHandler(MouseEvent.MOUSE_CLICKED, ev -> {
			displayToConsole("DEBUG: to end turn");
		});
		
		undo.addEventHandler(MouseEvent.MOUSE_CLICKED, ev -> {
			displayToConsole("This feature is unlockable via DLC.");
		});
		
		/*sort.addEventHandler(MouseEvent.MOUSE_CLICKED, ev -> {
			ObservableList<Node> uT = FXCollections.observableArrayList();
			uT = userTiles.getChildren();
			System.out.println(uT);
		});*/
		
		meldArea.setPrefWidth(350);
		
		canvas.setCenter(playGrid);
		canvas.setTop(topCommands);
		canvas.setBottom(userTiles);
		canvas.setLeft(meldArea);
		//userTiles.getChildren().addAll();
		
		playGame();
	}

	private void initCanvasElements() {
		// Console (side text are)
		console = new TextArea();
		console.setPrefHeight(900);
		console.setPrefWidth(300);
		//console.relocate(1155,5);
		console.setEditable(false);
		console.setMouseTransparent(true);
		console.setFocusTraversable(false);
		console.setWrapText(true);
		console.setId("console");
		
		console.textProperty().addListener((element, oldValue, newValue) -> {
			// Auto scroll to bottom when appending text to console pane
			console.setScrollTop(Double.MAX_VALUE);
		});
		
		// Preload the tile info
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
		
		// Start Button
		start = new Button("Start Game");
		//start.relocate(1155/2, 900/2);
		
		start.addEventHandler(MouseEvent.MOUSE_CLICKED, ev -> {
			displayToConsole("Started a game of Tile Rummy!");
			canvas.setCenter(null);
			initGameElements();
		});
		
		// Set elements on the BorderPane areas
		canvas.setRight(console);
		canvas.setCenter(start);
	}
}
