package core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Strat4 implements PlayerStrategy {

	@Override
	public int strat(Hand h, boolean initialMeld, List<ArrayList<Tile>> tableTiles, int min) {
		/*
		 * take table, calculate all the various numbers (i.e., calc # R1s, R2s, R3s etc. and # 1s, 2s, 3s etc. that are on the table)
		 * determine probabilities???
		 * Only need to compare them against the ones that are not in hand melds already (i.e., only need to compare the ones in "tilesForTableMelds" in Hand.java
		 * if chances are unlikely, attempt to "discard" them by playing them into table melds if possible
		 * 	otherwise, keep them in hand
		 * ret values
		 * 0 - draw
		 * 1 - played tiles
		 */
		
		HashMap<String, Integer> tileCounting = new HashMap<String, Integer>();
		ArrayList<ArrayList<Tile>> handMelds = new ArrayList<ArrayList<Tile>>(h.getHandMelds());
		ArrayList<Tile> tilesNotInHandMelds = new ArrayList<Tile>(h.getTilesForTableMelds());
		ArrayList<Tile> tilesForTableMelds = new ArrayList<Tile>();
		int sum = 0;
		
		// calculate the number of each tile and tile value is on the table
		for (ArrayList<Tile> meld: tableTiles) {
			for (Tile t: meld) {
				int tileVal = t.getValue();
				String tileString = t.toString();
				String tileValString = Integer.toString(tileVal);
				
				if (tileCounting.containsKey(tileString)) {
					int n = tileCounting.get(tileString);
					++n;
					tileCounting.put(tileString, n);
				} else {
					tileCounting.put(tileString, 1);
				}
				
				if (tileCounting.containsKey(tileValString)) {
					int n = tileCounting.get(tileValString);
					++n;
					tileCounting.put(tileValString, n);
				} else {
					tileCounting.put(tileValString, 1);
				}
			}
		}
		
		ArrayList<Tile> unworthyTilesNotInHandMelds = determineUnworthyRemainingTiles(tileCounting, tilesNotInHandMelds);
		
		for(ArrayList<Tile> meld : handMelds) {
			for(Tile tile : meld) {
				sum += tile.getValue();
			}
		}
		
		if(!initialMeld) {
			if(sum >= 30) {
				h.playHandMeld((ArrayList<ArrayList<Tile>>)tableTiles, false);
				return 1;
			}
		} else {
			h.playHandMeld((ArrayList<ArrayList<Tile>>) tableTiles, false);
			h.playTableMeld((ArrayList<ArrayList<Tile>>) tableTiles, unworthyTilesNotInHandMelds);
			if (h.getHandMelds().equals(handMelds) && h.getTilesForTableMelds().equals(tilesNotInHandMelds)) { // didn't play any hand melds
				return 0;
			}
			return 1;
		}
		
		return 0;
	}
	
	private ArrayList<Tile> determineUnworthyRemainingTiles(HashMap<String, Integer> tilesOnTheTable, ArrayList<Tile> remainingTilesFromHand){
		/**
		 * Determines whether or not to keep meld sizes of 2 or not
		 * If missing tile(s) to complete meld is on the table, discard them (i.e. play them as table melds if possible). Otherwise keep them.
		 */
		// at the end, those remaining in "handTiles" will be "discarded" to the table
		HashMap<Integer, ArrayList<Tile>> handTiles = new HashMap<Integer, ArrayList<Tile>>(13);
		ArrayList<Tile> remainingTiles = new ArrayList<Tile>();
		
		//add hand tiles to a map
		for (int idx = 0; idx < remainingTilesFromHand.size(); ++idx) {
			Tile tile = remainingTilesFromHand.get(idx);
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
		
		// find run melds of size 2, finds missing tiles on the right side (e.g., R4, R5, X)
		for (int tileValue = 1; tileValue <= 11; ++tileValue) {
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
					
					int nextTileValue = checkNextTileValue(tileValue+1, tileColour, handTiles, tempMeld);
					
					if (nextTileValue != -1) {
						String nextTileString = tileColour + Integer.toString(nextTileValue);
						
						if (!tilesOnTheTable.containsKey(nextTileString)) {
							// next tile to complete the meld is considered "likely", remove it from the search space
							iter.remove(); // remove the current tile
							for (int i = 1; i < tempMeld.size(); ++i) { //remove the other tiles in the meld (excluding current tile)
								Tile t = tempMeld.get(i);
								handTiles.get(t.getValue()).remove(t);
							}							
						} else {
							// next tile to complete the meld is considered "unlikely", keep it in the search space
						}
					}
				}
			}
		}
		
		// find run melds of size 2, finds missing tiles on the left side (e.g., X, R4, R5)
		for (int tileValue = 2; tileValue <= 12; ++tileValue) {
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
					
					int nextTileValue = checkNextTileValue(tileValue+1, tileColour, handTiles, tempMeld);
					
					if (nextTileValue != -1) {
						String nextTileString = tileColour + Integer.toString(nextTileValue-3);
						
						if (!tilesOnTheTable.containsKey(nextTileString)) {
							// next tile to complete the meld is considered "likely", remove it from the search space
							iter.remove(); // remove the current tile
							for (int i = 1; i < tempMeld.size(); ++i) { //remove the other tiles in the meld (excluding current tile)
								Tile t = tempMeld.get(i);
								handTiles.get(t.getValue()).remove(t);
							}							
						} else {
							// next tile to complete the meld is considered "unlikely", keep it in the search space
						}
					}
				}
			}
		}
		
		//find set melds of size 2
		for (int tileValue = 1; tileValue <= 13; ++tileValue) {
			if (handTiles.containsKey(tileValue) && handTiles.get(tileValue).size() >= 2) {
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
				
				// determine if the set meld of size 2 should be kept or not
				if (tempMeld.size() == 2) {
					ArrayList<String> c = new ArrayList<String>(Arrays.asList("R", "B", "G", "O"));
					int numTrues = 0;
					c.removeAll(colours);
					
					for (String colour: c) {
						String nextTileString = colour + Integer.toString(tileValue);
						if (tilesOnTheTable.containsKey(nextTileString)) {
							++numTrues;
						}
					}
					
					if (numTrues == c.size()) {
						// keep the tiles in the search space, as it is "unlikely" to complete the set meld b/c
						//     the 2 remaining tiles to complete it is already on the table
					} else {
						// remove the tiles from search space, as it is "likely" to complete the set meld
						for (int i = 0; i < tempMeld.size(); ++i) {
							Tile t = tempMeld.get(i);
							handTiles.get(t.getValue()).remove(t);
						}
					}
				}
			}
		}
		
		//remaining tiles, check if there are 5/8 of the tiles on the table already
		for (int tileValue = 1; tileValue <= 13; ++tileValue) {
			if (handTiles.containsKey(tileValue) && tilesOnTheTable.containsKey(Integer.toString(tileValue))) {
				int numTilesOnTable = tilesOnTheTable.get(Integer.toString(tileValue));
				if (numTilesOnTable >= 5) {
					// keep tiles in the search space, as it is an unworthy tile to keep in the hand
				} else {
					// remove from the search space, as it is worthy enough to keep in hand (there is not >= 5 of the tile value on the table)
					handTiles.get(tileValue).clear();
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
		
		return remainingTiles;
	}
	
	private int checkNextTileValue(int nextTileValue, String tileColour, HashMap<Integer, ArrayList<Tile>> handTiles, ArrayList<Tile> tempMeld) {
		/**
		 * @param tileValue The next tile value to check for
		 * @param tileColour The tile colour to look for
		 * @param handTiles The hand tiles
		 * @param tempMeld The current "potential" meld 
		 * @return The next tile value to complete the meld or -1 if there isn't 2 tiles in the unfinished meld
		 */
		if (handTiles.containsKey(nextTileValue)) {
			ArrayList<Tile> nextTiles = handTiles.get(nextTileValue);
			
			for (Tile tile: nextTiles) {
				String[] tileInfo = tile.getInfo();
				
				if (tileInfo[0].equals(tileColour)) {
					tempMeld.add(tile);
					return nextTileValue+1;
				}
			}
		}
		
		// didn't find second connecting tile 
		return -1;
	}
}
