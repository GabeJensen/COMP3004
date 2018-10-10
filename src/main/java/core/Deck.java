package core;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {
	private ArrayList<Tile> deck;
	
	public Deck() {
		deck = new ArrayList<Tile>();
		String[] colors = {"R", "B", "G", "O"};
		String[] values = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13"};
		
		int vLength = values.length;
		
		for (int d = 0; d < 2; d++) {
			for (int c = 0; c < colors.length; c++) {
				for (int v = 0; v < vLength; v++) {
					deck.add(new Tile(colors[c], values[v]));
				}
			}
		}
	}
	
	public void shuffleDeck() {
		Collections.shuffle(deck);
	}
	
	public Tile dealTile() {
		if (deck.size() < 1) {
			return null;
		}
		else {
			return deck.remove(deck.size() - 1);
		}
	}
	
	public int getTileCount() {
		return deck.size();
	}
}
