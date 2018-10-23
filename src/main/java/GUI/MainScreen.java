package GUI;

import java.io.File;
import java.util.HashMap;
import java.util.regex.Pattern;

import core.Game;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

public class MainScreen extends Application {
	private Game game;
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
		game = new Game();
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
		
		canvas.setCenter(playGrid);
		canvas.setTop(topCommands);
		canvas.setBottom(userTiles);
		//userTiles.getChildren().addAll();
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
			game.playGame();
		});
		
		// Set elements on the BorderPane areas
		canvas.setRight(console);
		canvas.setCenter(start);
	}
}
