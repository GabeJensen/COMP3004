package GUI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainScreen extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		initWindow(primaryStage);
	}

	private void initWindow(Stage primaryStage) {
		Pane canvas = new Pane();
		canvas.setStyle("-fx-background-color: #660f91"); //brown-ish colour
		
		//add labels/buttons/images
		
		Scene scene = new Scene(canvas, 1280, 720);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Team 8 - Tile Rummy");
		primaryStage.show();
	}

}
