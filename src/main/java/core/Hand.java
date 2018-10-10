package core;

import java.util.ArrayList;
import java.util.Collections;

public class Hand {
	private ArrayList<Tile> hand;
	
	public Hand() {
		hand = new ArrayList<Tile>();
	}
	
	public ArrayList<Tile> getHand() {
		return hand;
	}
	
	public void displayHand() {
		for (int i = 0; i < hand.size(); i++) {
			System.out.println(hand.get(i).toString());
		}
	}
	
	public void sortTiles() {
		Collections.sort(hand, new TileComparator());
	}
	
	public int getCount() {
		return hand.size();
	}
	
	public void addTile(Tile t) {
		hand.add(t);
	}
	
	public Tile removeTile(int index) {
		if(index + 1 > hand.size()) {
			return null;
		} else {
			return hand.remove(index);
		}
	}
	
	public ArrayList<Tile> removeTiles(ArrayList<Integer> indices){
		ArrayList<Tile> returnTiles = new ArrayList<Tile>();
		for (Integer index : indices) {
			if(index+1 > hand.size()) {
				return null;
			} else {
				returnTiles.add(hand.get(index));				
			}
		}
		
		for (Integer index : indices) {
			if(index+1 > hand.size()) {
				return null;
			} else {
				hand.remove(index);				
			}
		}
		
		return returnTiles;
	}
}