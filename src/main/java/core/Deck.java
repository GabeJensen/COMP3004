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
		
		deck.add(new Tile("R", "J"));
		deck.add(new Tile("R", "J"));
		
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
	
	public Tile drawSpecific(String t) {
		for (Tile tile : deck) {
			if (tile.toString().equals(t)) {
				Tile instance = tile;
				deck.remove(tile);
				return instance;
			}
		}
		return null;
	}
	
	public ArrayList<String> getDeckString() {
		ArrayList<String> strDeck = new ArrayList<String>();
		for (Tile t : deck) {
			strDeck.add(t.toString());
		}
		Collections.sort(strDeck);
		return strDeck;
	}
	
	public int getTileCount() {
		return deck.size();
	}
}
