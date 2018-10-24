package GUI;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class DisplayTile {	
	public ImageView iv;
	private double lastDragX;
	private double lastDragY;
	
	public DisplayTile(Image tile) {
		iv = new ImageView();
		createDisplayTile(tile);
	}
	
	private void createDisplayTile(Image tile) {
		//Source: https://stackoverflow.com/questions/47517197/moving-an-image-using-mouse-drag-events
		iv.setImage(tile);
		iv.setFitWidth(55);
		iv.setPreserveRatio(true);
		
		iv.setOnMousePressed(ev -> {
			lastDragX = ev.getSceneX();
			lastDragY = ev.getSceneY();
		});
		
		iv.setOnMouseDragged(ev -> {
			iv.setTranslateX(iv.getTranslateX() + (ev.getSceneX() - lastDragX));
			iv.setTranslateY(iv.getTranslateY() + (ev.getSceneY() - lastDragY));
			lastDragX = ev.getSceneX();
			lastDragY = ev.getSceneY();
		});
	}
}
