package GUI;

import core.Tile;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class DisplayTile {	
	public ImageView iv;
	public Tile tile;
	public boolean isOrigin;
	public int lastIndex;
	private double lastX;
	private double lastY;
	
	public DisplayTile(Image tile, Tile obj) {
		iv = new ImageView();
		this.tile = obj;
		isOrigin = true;
		createDisplayTile(tile);
	}
	
	public void setOriginCoordinates(double x, double y) {
		lastX = x;
		lastY = y;
	}
	
	private void createDisplayTile(Image tile) {
		//Source: https://stackoverflow.com/questions/47517197/moving-an-image-using-mouse-drag-events
		iv.setImage(tile);
		iv.setFitWidth(40);
		iv.setPreserveRatio(true);
		
		/*iv.setOnMousePressed(ev -> {
			lastDragX = ev.getSceneX();
			lastDragY = ev.getSceneY();
		});
		
		iv.setOnMouseDragged(ev -> {
			iv.setTranslateX(iv.getTranslateX() + (ev.getSceneX() - lastDragX));
			iv.setTranslateY(iv.getTranslateY() + (ev.getSceneY() - lastDragY));
			lastDragX = ev.getSceneX();
			lastDragY = ev.getSceneY();
		});*/
	}
}
