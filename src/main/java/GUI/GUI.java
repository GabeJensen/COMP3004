package GUI;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import core.Meld;
import core.Tile;
import core.TileRummyGame;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
	private static HashMap<String, Image> imageLoc;
	private static HashMap<ImageView, Tile> associatedTiles;
	private static TextArea consoleTextArea;
	private Button startButton;
	private CheckBox modeCbox;
	private static boolean mode;
	private static Button endButton;
	private static Button audioToggle;
	private boolean audioMuted = false;
	private static Label rigTileDrawLabel;
	private static ComboBox<String> remainingDeckTileDropdown;
	private static Button playMeldButton;
	private static Label time;
	private CheckBox timeCheckbox;
	private static boolean useTimer;
	private static VBox playMeldBox;
	private static ScrollPane horizScroll;
	private static HBox userTilesBox;
	private static HBox selectionContainer;
	private static ArrayList<String> strategySelection;
	private static ComboBox<Integer> playerCount;
	private static ComboBox<Integer> userCount;
	private HBox topCommandsBox;
	private static TilePane playGrid;
	private static List<ArrayList<Tile>> currentTurnMelds;
	private static ArrayList<Tile> currentTurnUserUsedTiles;
	private final InnerShadow handPlayEffect = new InnerShadow(20, Color.RED);
	private final InnerShadow tablePlayEffect = new InnerShadow(20, Color.BLUE);
	private static ArrayList<Tile> tablePlayTiles;
	public final static String regularDraw = "Regular Tile Draw";
	private TileRummyGame rummyGame;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// Init the users' temporary tile tracking array
		GUI.currentTurnMelds = new ArrayList<ArrayList<Tile>>(); // keeps track of the melds played throughout the turn
		GUI.currentTurnUserUsedTiles = new ArrayList<Tile>(); // keeps track of the tiles used from the hand
		GUI.tablePlayTiles = new ArrayList<Tile>(); // keeps track of the tiles that have been re-used from the table (only used for updateTempMelds())
		
		// Create our rummyGame "controller" object and initialize our window
		this.rummyGame = new TileRummyGame();
		initWindow(primaryStage);
	}
	
	@Override
	public void stop() {
		try {
			rummyGame.stopTimer();
			System.exit(0);
		}
		catch (Exception e) {
			System.exit(0);
			// no instance of timer was run.
		}
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
		
		// Top container - Reading a file location
		Label fRead = new Label("File to Read Data");
		fRead.setStyle("-fx-background-color: white;");
		TextField fileLocation = new TextField();
		fRead.setLabelFor(fileLocation);
		fileLocation.setPromptText("Example: src/test/resources/");
		fileLocation.setPrefWidth(300);
		
		HBox fileElements = new HBox();
		fileElements.setSpacing(10);
		fileElements.setAlignment(Pos.CENTER);
		
		
		// Middle container - Labels, Dropdowns and the Start Game Button
		selectionContainer = new HBox();
		selectionContainer.setSpacing(10);
		
		// Start Button
		startButton = new Button("Start Game");
		
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
			canvas.setTop(null);
			if (!fileLocation.getText().equals("")) {
				rummyGame.loadFile(fileLocation.getText());
			}
			mode = modeCbox.isSelected();
			useTimer = timeCheckbox.isSelected();
			music();
			initGameElements();
		});
		
		Label gameMode = new Label("Testing Mode");
		modeCbox = new CheckBox();
		
		Label timerOptionLabel = new Label("Use Timer for Users");
		timeCheckbox = new CheckBox();
		
		gameMode.setLabelFor(playerCount);
		timerOptionLabel.setLabelFor(timeCheckbox);
		gameMode.setStyle("-fx-background-color: white;");
		timerOptionLabel.setStyle("-fx-background-color: white");
		fileElements.getChildren().addAll(timerOptionLabel, timeCheckbox, gameMode, modeCbox, fRead, fileLocation);		
		
		Label pCount = new Label("Players");
		Label uCount = new Label("Users");
		
		playerCount = new ComboBox<Integer>();
		playerCount.getItems().addAll(2, 3, 4);
				
		userCount = new ComboBox<Integer>();
		userCount.getItems().addAll(0, 1, 2, 3, 4);
		
		playerCount.getSelectionModel().selectFirst();
		userCount.getSelectionModel().select(1);
		
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
		canvas.setTop(fileElements);
		canvas.setCenter(selectionContainer);
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				playerCount.requestFocus();
			}
		});
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
			container.getItems().addAll("Strategy 1", "Strategy 2", "Strategy 3", "Strategy 4");
			container.getSelectionModel().selectFirst();
			selectionContainer.getChildren().addAll(s_down, container);
		}
		return;
	}
	
	private void music() {
		Media media = new Media(Paths.get("src/main/resources/o.mp3").toUri().toString());
		m = new MediaPlayer(media);
		m.setCycleCount(MediaPlayer.INDEFINITE);
		m.play();
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
		
		rummyGame.initializeGame(userCount.getValue(), strategySelection);
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
		imageLoc = new HashMap<String, Image>();
		associatedTiles = new HashMap<ImageView, Tile>();
		for (int i = 0; i < tileImages.length; i++) {
			String fileName = tileImages[i].getName();
			String[] imgInf = fileName.split(Pattern.quote("."));
			imageLoc.put(imgInf[0], new Image(new File(tileResources + tileImages[i].getName()).toURI().toString()));
		}
		String jokerName = "J.png";
		Image jokerImg = new Image(new File(tileResources + jokerName).toURI().toString());
		imageLoc.put("RJ", jokerImg);
		imageLoc.put("BJ", jokerImg);
		imageLoc.put("OJ", jokerImg);
		imageLoc.put("GJ", jokerImg);
//		imageLoc.put("J", tileResources + jokerName);
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
		
		audioToggle = new Button("Toggle Audio");
		audioToggle.addEventFilter(MouseEvent.MOUSE_CLICKED, ev -> {
			audioMuted = !audioMuted;
			if (audioMuted) {
				audioToggle.setText("Unmute Audio");
			}
			else {
				audioToggle.setText("Mute Audio");
			}
			m.setMute(audioMuted);
		});
		/*undoButton = new Button("Undo Turn");
		undoButton.addEventHandler(MouseEvent.MOUSE_CLICKED, ev -> {
			playMeldBox.getChildren().remove(1, playMeldBox.getChildren().size());
			
			// Wipe the turn's tracking information, turn getting reset to beginning
			currentTurnMelds.clear();
			currentTurnUserUsedTiles.clear();
			
			rummyGame.undoTurn();
		});*/
		topCommandsBox.getChildren().addAll(endButton, audioToggle);
		if (useTimer) {
			time = new Label();
			time.setStyle("-fx-background-color: white; -fx-padding: 5 10 5 10");
			topCommandsBox.getChildren().addAll(time);
		}
		if (mode) {
			rigTileDrawLabel = new Label("Set Next Tile to Draw");
			rigTileDrawLabel.setStyle("-fx-background-color: white;");
			rigTileDrawLabel.setLabelFor(remainingDeckTileDropdown);
			remainingDeckTileDropdown = new ComboBox<String>();
			remainingDeckTileDropdown.getItems().add(regularDraw);
			topCommandsBox.getChildren().addAll(rigTileDrawLabel, remainingDeckTileDropdown);
		}		 
	}
	
	public static boolean getTimeOption() {
		return useTimer;
	}
	
	public static boolean getGameMode() {
		return mode;
	}
	
	public static void updateTileDrawDropdown(ArrayList<String> deckString) {
		remainingDeckTileDropdown.getItems().clear();
		remainingDeckTileDropdown.getItems().add(regularDraw);
		for (String tile : deckString) {
			remainingDeckTileDropdown.getItems().add(tile);
		}
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				remainingDeckTileDropdown.getSelectionModel().selectFirst();
			}
		});
	}
	
	public static String getTileDrawStatus() {
		if (remainingDeckTileDropdown.getValue() == null) {
			return regularDraw;
		}
		return remainingDeckTileDropdown.getValue();
	}
	
	public static void displayToConsole(String s) {
		consoleTextArea.appendText(s + "\n");
	}
	
	public static void updateTime(int t) {
		time.setText("Time: " + Integer.toString(t));
	}
	
/*	private static void removeImageReference(ArrayList<DisplayTile> holder) {
		for (int tc = 0; tc < holder.size(); tc++) {
			associatedTiles.remove(holder.get(tc).iv);
		}
		holder.clear();
	}*/
	
	public static void undoTurn() {
		playMeldBox.getChildren().remove(1, playMeldBox.getChildren().size());
		
		// Wipe the turn's tracking information, reset turn to beginning.
		currentTurnMelds.clear();
		currentTurnUserUsedTiles.clear();
	}
	
	private static void clearUnusedIV() {
		ArrayList<ImageView> unusedImageViews = new ArrayList<ImageView>();
		associatedTiles.forEach((iv, t) -> {
			if (!(playGrid.getChildren().contains(iv)) && !(userTilesBox.getChildren().contains(iv))) {
				unusedImageViews.add(iv);
			}
		});
		for (ImageView i : unusedImageViews) {
			associatedTiles.remove(i);
		}
	}
	
	private void updateTempMelds() {
		clearUnusedIV();
		for (int x = currentTurnMelds.size() - 1; x < currentTurnMelds.size(); x++) {
			for (Tile meldTile : currentTurnMelds.get(x)) {
				Image tile = imageLoc.get(meldTile.toString());
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
	
	public static void updateDisplayTable(List<ArrayList<Tile>> table) {
		clearDisplayTable();
		for (ArrayList<Tile> meld : table) {
			for (Tile meldTile : meld) {
				Image tile = imageLoc.get(meldTile.toString());	
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
	
	private static void clearDisplayTable () {
		clearUnusedIV();
		playGrid.getChildren().clear();
	}
	
	public static void updateDisplayHand(ArrayList<Tile> hand) {
		clearDisplayHand();
		for (Tile userTile : hand) {
			Image tile = imageLoc.get(userTile.toString());		
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
	
	private static void clearDisplayHand() {
		clearUnusedIV();
		userTilesBox.getChildren().clear();
	}
	
	public static void disableButtons() {
		endButton.setDisable(true);
		playMeldButton.setDisable(true);
	}
}
