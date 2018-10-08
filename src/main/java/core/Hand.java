package core;

import java.util.ArrayList;
import java.util.Collections;

public class Hand {
	private ArrayList<Tile> hand = new ArrayList<Tile>();
	
	public Hand() {
		// 
	}
	
	// public ??? getHand?
	
	public void sortTiles() {
		Collections.sort(hand, new TileComparator());
	}
	
	public int getCount() {
		return hand.size();
	}
	
	public void addTile(Tile t) {
		hand.add(t);
	}
}