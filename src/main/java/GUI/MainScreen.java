package GUI;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import core.Deck;
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
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import tableObserver.Table;

public class MainScreen extends Application {
	public Table table;
	public Player user;
	public Player p1;
	public Player p2;
	public Player p3;
	private Deck deck;
	
	private BorderPane canvas;
	private Scene scene;
	private HashMap<String, String> imageLoc;
	private TextArea console;
	private Button start;
	private Button end;
	private Button undo;
	private HBox userTiles;
	private HBox topCommands;
	private TilePane playGrid;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// Init things here.
		this.table = new Table();
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
		
		updateDisplayHand();
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
	
	private void updateDisplayHand() {
		clearDisplayHand();
		ArrayList<Tile> userHand = user.getTiles();
		for (Tile userTile : userHand) {
			Image tile = new Image(new File(imageLoc.get(userTile.toString())).toURI().toString());
			ImageView iv = new ImageView();
			iv.setImage(tile);
			iv.setFitWidth(55);
			iv.setPreserveRatio(true);
			userTiles.getChildren().addAll(iv);
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
		// Memento pattern. 
		undo.setDisable(true);
		// Remove the above button when undo is actually implemented.
		topCommands.getChildren().addAll(end, undo);
		
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
		
		canvas.setCenter(playGrid);
		canvas.setTop(topCommands);
		canvas.setBottom(userTiles);
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
