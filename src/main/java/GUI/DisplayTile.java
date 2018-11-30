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
	
	public DisplayTile(Image tile, Tile obj) {
		iv = new ImageView();
		this.tile = obj;
		isOrigin = true;
		createDisplayTile(tile);
	}
	
	private void createDisplayTile(Image tile) {
		iv.setImage(tile);
		iv.setFitWidth(40);
		iv.setPreserveRatio(true);
	}
}
