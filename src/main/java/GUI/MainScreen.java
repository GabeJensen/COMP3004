package GUI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainScreen extends Application {
	
	private Pane canvas;
	private Scene scene;
	TextArea console;
	Button start;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		initWindow(primaryStage);
	}

	private void initWindow(Stage primaryStage) {
		Pane canvas = new Pane();
		this.canvas = canvas;
		canvas.setId("pane");
		
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

	private void initCanvasElements() {
		// Console (side text are)
		console = new TextArea();
		console.setPrefHeight(900);
		console.setPrefWidth(300);
		console.relocate(1155,5);
		console.setEditable(false);
		console.setMouseTransparent(true);
		console.setFocusTraversable(false);
		console.setWrapText(true);
		console.setId("console");
		
		console.textProperty().addListener((element, oldValue, newValue) -> {
			// Auto scroll to bottom when appending text to console pane
			console.setScrollTop(Double.MAX_VALUE);
		});
		
		// Start Button
		start = new Button("Start Game");
		start.relocate(1155/2, 900/2);
		
		start.addEventHandler(MouseEvent.MOUSE_CLICKED, ev -> {
			displayToConsole("Started a game of Tile Rummy!");
			canvas.getChildren().remove(start);
		});
		
		// Add all elements created to primary canvas node here.
		canvas.getChildren().addAll(console, start);
	}
}
