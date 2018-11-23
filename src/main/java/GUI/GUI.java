package GUI;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import core.Meld;
import core.Tile;
import core.TileRummyGame;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
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

public class GUI extends Application {
	private static MediaPlayer m;
	private BorderPane canvas;
	private Scene scene;
	private static HashMap<String, String> imageLoc;
	private static HashMap<ImageView, Tile> associatedTiles;
	private static TextArea consoleTextArea;
	private Button startButton;
	private static Button endButton;
	private static Button playMeldButton;
	private static Label time;
	private static VBox playMeldBox;
	private static ScrollPane horizScroll;
	private static HBox userTilesBox;
	private static HBox selectionContainer;
	private static ArrayList<String> strategySelection;
	private static ComboBox<Integer> playerCount;
	private static ComboBox<Integer> userCount;
	private static HBox topCommandsBox;
	private static TilePane playGrid;
	private static List<ArrayList<Tile>> currentTurnMelds;
	private static ArrayList<Tile> currentTurnUserUsedTiles;
	private final InnerShadow handPlayEffect = new InnerShadow(20, Color.RED);
	private final InnerShadow tablePlayEffect = new InnerShadow(20, Color.BLUE);
	private static ArrayList<Tile> tablePlayTiles;
	private static ArrayList<DisplayTile> dtTabRef;
	private static ArrayList<DisplayTile> dtHandRef;
	private static ArrayList<DisplayTile> dtTempRef;
	private TileRummyGame rummyGame;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// Init the users' temporary tile tracking array
		GUI.currentTurnMelds = new ArrayList<ArrayList<Tile>>(); // keeps track of the melds played throughout the turn
		GUI.currentTurnUserUsedTiles = new ArrayList<Tile>(); // keeps track of the tiles used from the hand
		GUI.tablePlayTiles = new ArrayList<Tile>(); // keeps track of the tiles that have been re-used from the table (only used for updateTempMelds())
		
		// Create out rummyGame "controller" object and initialize our window
		this.rummyGame = new TileRummyGame();
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
	
	private void initCanvasElements() {
		// Console (side text are)
		initConsole();
		
		// Preload the tile info
		loadTileImages();
		
		// Start Button
		startButton = new Button("Start Game");
		//startButton.relocate(1155/2, 900/2);
		
		startButton.addEventHandler(MouseEvent.MOUSE_CLICKED, ev -> {
			if (userCount.getValue() > playerCount.getValue()) {
				displayToConsole("You cannot have more users than total players!");
				return;
			}
			
			strategySelection = new ArrayList<String>();
			for (int x = 4; x < selectionContainer.getChildren().size(); x+=2) {
				if (selectionContainer.getChildren().get(x) instanceof Button) {
					// is a button, don't do anything.
					// This is here as we might not add anything with the 0 users.
				} 
				else {
					strategySelection.add((String)((ComboBox) selectionContainer.getChildren().get(x)).getValue());
				}	
			}
			
			displayToConsole("Started a game of Tile Rummy!");
			canvas.setCenter(null);
			music();
			initGameElements();
		});
		
		selectionContainer = new HBox();
		selectionContainer.setSpacing(10);
		
		Label pCount = new Label("Players");
		Label uCount = new Label("Users");
		
		playerCount = new ComboBox<Integer>();
		playerCount.getItems().addAll(2, 3, 4);
				
		userCount = new ComboBox<Integer>();
		userCount.getItems().addAll(0, 1, 2, 3, 4);
		
		playerCount.getSelectionModel().selectFirst();
		userCount.getSelectionModel().selectFirst();
		
		pCount.setLabelFor(playerCount);
		pCount.setStyle("-fx-background-color: white;");
		uCount.setLabelFor(userCount);
		uCount.setStyle("-fx-background-color: white;");
		
		playerCount.setOnAction((ev) -> {
			rerenderStratDropdown();
		});
		
		userCount.setOnAction((ev) -> {
			rerenderStratDropdown();
		});
		
		selectionContainer.getChildren().addAll(pCount, playerCount, uCount, userCount, startButton);
		renderStratDropdown();
		selectionContainer.setAlignment(Pos.CENTER);
		
		// Set elements on the BorderPane areas
		canvas.setRight(consoleTextArea);
		canvas.setCenter(selectionContainer);
	}
	
	private void rerenderStratDropdown() {
		selectionContainer.getChildren().remove(5, selectionContainer.getChildren().size());
		renderStratDropdown();
		return;
	}
	
	private void renderStratDropdown() {
		for (int x = 0; x < (playerCount.getValue() - userCount.getValue()); x++) {
			ComboBox<String> container = new ComboBox<String>();
			Label s_down = new Label("P" + (x+1) + " Strategy");
			s_down.setLabelFor(container);
			s_down.setStyle("-fx-background-color: white;");
			container.setId("P" + (x+1));
			container.getItems().addAll("Strategy 1", "Strategy 2", "Strategy 3");
			container.getSelectionModel().selectFirst();
			selectionContainer.getChildren().addAll(s_down, container);
		}
		return;
	}
	
	private void music() {
		Media media = new Media(Paths.get("src/main/resources/S.mp3").toUri().toString());
		m = new MediaPlayer(media);
		m.setCycleCount(MediaPlayer.INDEFINITE);
		m.play();
	}
	
	public static void destroyTime() {
		topCommandsBox.getChildren().remove(time);
	}

	private void initGameElements() {
		// Bottom card section
		horizScroll = new ScrollPane();
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
		horizScroll.setContent(userTilesBox);
		canvas.setBottom(horizScroll);
		canvas.setLeft(playMeldBox);
		//userTilesBox.getChildren().addAll();
		
		dtTabRef = new ArrayList<DisplayTile>();
		dtHandRef = new ArrayList<DisplayTile>();
		dtTempRef = new ArrayList<DisplayTile>();
		
		rummyGame.initalizeGame(userCount.getValue(), strategySelection);
		rummyGame.playGame();
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
		String tileResources = "src/main/resources/tiles/";
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
			// Obtain the table state that is displayed in the GUI
			List<ArrayList<Tile>> table = new ArrayList<ArrayList<Tile>>();
			ArrayList<Tile> meld = new ArrayList<Tile>();
			for (int i = 0; i < playGrid.getChildren().size(); ++i) {
				//hit blank space, add to newTable if size != 0
				if (playGrid.getChildren().get(i) instanceof Region) {
					if (meld.size() == 0) {
						continue;
					} else {
						table.add(meld);
						meld = new ArrayList<Tile>();
					}
				}else if (playGrid.getChildren().get(i) instanceof ImageView) {
					//add tile to meld
					ImageView tileIV = (ImageView) playGrid.getChildren().get(i);
					meld.add(associatedTiles.get(tileIV));
				}
			}
			
			int retVal = rummyGame.endTurn(currentTurnUserUsedTiles, currentTurnMelds, table, playMeldBox.getChildren().size());
			
			if (retVal == 0) {
				// Wipe the turn's tracking information, end of turn
				currentTurnMelds.clear();
				currentTurnUserUsedTiles.clear();
			} else {
				// Still tiles in the playMeldBox
			}
		});
		
		time = new Label();
		time.setStyle("-fx-background-color: white; -fx-padding: 5 10 5 10");
		
		topCommandsBox.getChildren().addAll(endButton, time);
	}

	public static void undoTurn() {
		playMeldBox.getChildren().remove(1, playMeldBox.getChildren().size());
		
		// Wipe the turn's tracking information, turn getting reset to beginning
		currentTurnMelds.clear();
		currentTurnUserUsedTiles.clear();
	}
	
	public static void displayToConsole(String s) {
		consoleTextArea.appendText(s + "\n");
	}
	
	public static void updateTime(int t) {
		if (time != null) {
			time.setText("Time: " + Integer.toString(t));
		}
	}
	
	private static void removeImageReference(ArrayList<DisplayTile> holder) {
		for (int tc = 0; tc < holder.size(); tc++) {
			associatedTiles.remove(holder.get(tc).iv);
		}
		holder.clear();
	}
	
	private static void clearUnusedIV() {
		/*for (HashMap.Entry<ImageView, Tile> mapping : associatedTiles.entrySet()) {
			ImageView id = mapping.getKey();
			if (!(playGrid.getChildren().contains(id)) && !(userTilesBox.getChildren().contains(id))) {
				associatedTiles.remove(id);
			}
		}*/
		/*associatedTiles.forEach((iv, t) -> {
			if (!(playGrid.getChildren().contains(iv)) && !(userTilesBox.getChildren().contains(iv))) {
				associatedTiles.remove(iv);
			}
		});*/
	}
	
	private void updateTempMelds() {
		//removeImageReference(dtTempRef);
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
				dtTempRef.add(dTile);
			}
			playGrid.getChildren().add(new Region());
		}
		tablePlayTiles.clear();
	}
	
	public static void updateDisplayTable(List<ArrayList<Tile>> table) {
		clearDisplayTable();
		for (ArrayList<Tile> meld : table) {
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
				dtTabRef.add(dTile);
			}
			playGrid.getChildren().add(new Region());
		}
	}
	
	private static void clearDisplayTable () {
		//removeImageReference(dtTabRef);
		clearUnusedIV();
		playGrid.getChildren().clear();
	}
	
	public static void updateDisplayHand(ArrayList<Tile> hand) {
		clearDisplayHand();
		for (Tile userTile : hand) {
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
			dtHandRef.add(dTile);
		}
	}
	
	private static void clearDisplayHand() {
		//removeImageReference(dtHandRef);
		clearUnusedIV();
		userTilesBox.getChildren().clear();
	}
	
	public static void disableButtons() {
		endButton.setDisable(true);
		playMeldButton.setDisable(true);
	}
}
