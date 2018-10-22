package core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Hand {
	private ArrayList<Tile> hand;
	private List<ArrayList<Tile>> handMelds;
	private ArrayList<Tile> tilesForTableMelds;
	
	public Hand() {
		hand = new ArrayList<Tile>();
		handMelds = new ArrayList<ArrayList<Tile>>();
		tilesForTableMelds = new ArrayList<Tile>();
	}
	
	public ArrayList<Tile> getHand() {
		return hand;
	}
	
	public List<ArrayList<Tile>> getHandMelds(){
		return handMelds;
	}
	
	public ArrayList<Tile> getTilesForTableMelds(){
		return tilesForTableMelds;
	}
	
	public void displayHand() {
		for (int i = 0; i < hand.size(); i++) {
			System.out.println(hand.get(i).toString());
		}
	}
	
	public int getCount() {
		return hand.size();
	}
	
	public void addTile(Tile t) {
		hand.add(t);
		sortTiles();
		determineHandMeldsAndTilesForTableMelds();
	}
	
	public Tile removeTile(int index) {
		if(index + 1 > hand.size()) {
			return null;
		} else {
			Tile rmTile = hand.remove(index);
			determineHandMeldsAndTilesForTableMelds();
			return rmTile;
		}
	}
	
	public Tile removeTile(Tile remove) {
		int index = hand.indexOf(remove);
		if(index == -1) {
			return null;
		} else {
			Tile rmTile = hand.remove(index);
			determineHandMeldsAndTilesForTableMelds();
			return rmTile;
		}
	}
	
	public ArrayList<Tile> removeTiles(ArrayList<Integer> indices){
		ArrayList<Tile> returnTiles = new ArrayList<Tile>();
		for (Integer index : indices) {
			if(index+1 > hand.size()) {
				return null;
			} else {
				returnTiles.add(hand.get(index));	
				hand.remove(index);
			}
		}
		
		determineHandMeldsAndTilesForTableMelds();
		return returnTiles;
	}
	
	private void sortTiles() {
		Collections.sort(hand, new TileComparator());
	}
	
	private void determineHandMeldsAndTilesForTableMelds() {
		HashMap<Integer, ArrayList<Tile>> handTiles = new HashMap<Integer, ArrayList<Tile>>(13);
		List<ArrayList<Tile>> validMelds = new ArrayList<ArrayList<Tile>>();
		ArrayList<Tile> remainingTiles = new ArrayList<Tile>();
		
		//add hand tiles to a map
		for (int idx = 0; idx < hand.size(); ++idx) {
			Tile tile = hand.get(idx);
			//key = tile value, value = tile
			int key = tile.getValue();
			if (handTiles.containsKey(key)) {
				//if the tile value is in the map, add new tile to the list
				handTiles.get(key).add(tile);
			} else {
				//if the tile value is not in the map, add it to a new list of tiles
				ArrayList<Tile> tiles = new ArrayList<Tile>();
				tiles.add(tile);
				handTiles.put(key, tiles);
			}
		}
		
		//find run melds
		for (int tileValue = 1; tileValue <= 10; ++tileValue) {
			if (handTiles.containsKey(tileValue)) {
				Iterator<Tile> iter = handTiles.get(tileValue).iterator();
				
				//iterate through tiles where their tile values == tileValue
				while (iter.hasNext()) {
					Tile tile = iter.next();
					ArrayList<Tile> tempMeld = new ArrayList<Tile>();
					tempMeld.add(tile);
					
					//get this tile's info
					String[] tileInfo = tile.getInfo();
					String tileColour = tileInfo[0];
					
					checkNextTileValue(tileValue+1, tileColour, handTiles, tempMeld);
					
					if (tempMeld.size() > 2) {
						//remove tiles in tempMeld from search space
						iter.remove(); // remove the current tile
						for (int i = 1; i < tempMeld.size(); ++i) { //remove the other tiles in the meld (excluding current tile)
							Tile t = tempMeld.get(i);
							handTiles.get(t.getValue()).remove(t);
						}
						validMelds.add(tempMeld);
					}
				}
			}
		}
		
		//find set melds
		for (int tileValue = 1; tileValue <= 13; ++tileValue) {
			if (handTiles.containsKey(tileValue) && handTiles.get(tileValue).size() > 2) {
				ArrayList<String> colours = new ArrayList<String>();
				ArrayList<Tile> tempMeld = new ArrayList<Tile>();
				
				for (Tile tile: handTiles.get(tileValue)) {	
					//get this tile's info
					String[] tileInfo = tile.getInfo();
					String tileColour = tileInfo[0];
					
					if (!colours.contains(tileColour)) {
						colours.add(tileColour);
						tempMeld.add(tile);
					}
				}
				
				if (tempMeld.size() > 2) {
					// remove tiles in tempMeld from search space
					for (int i = 0; i < tempMeld.size(); ++i) {
						Tile t = tempMeld.get(i);
						handTiles.get(t.getValue()).remove(t);
					}
					validMelds.add(tempMeld);
				}
			}
		}
		
		// gather the remaining tiles that will be used for table melds
		Iterator<Integer> itr = handTiles.keySet().iterator();
		while (itr.hasNext()) {
			int key = itr.next();
			
			if (!handTiles.get(key).isEmpty()) {
				for (Tile t: handTiles.get(key)) {
					remainingTiles.add(t);
				}
			}
		}
		
		handMelds = validMelds;
		tilesForTableMelds = remainingTiles;
	}
	
	private void checkNextTileValue(int nextTileValue, String tileColour, HashMap<Integer, ArrayList<Tile>> handTiles, ArrayList<Tile> tempMeld) {
		/**
		 * @param tileValue The next tile value to check for
		 * @param tileColour The tile colour to look for
		 * @param handTiles The hand tiles
		 * @param tempMeld The current "potential" meld 
		 */
		if (handTiles.containsKey(nextTileValue)) {
			ArrayList<Tile> nextTiles = handTiles.get(nextTileValue);
			
			for (Tile tile: nextTiles) {
				String[] tileInfo = tile.getInfo();
				
				if (tileInfo[0].equals(tileColour)) {
					tempMeld.add(tile);
					checkNextTileValue(nextTileValue+1, tileColour, handTiles, tempMeld);
					break;
				}
			}
		}
	}
}