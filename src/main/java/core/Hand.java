package core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

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
	
	public void setHand(ArrayList<Tile> newHand) {
		this.hand = newHand;
	}
	
	public List<ArrayList<Tile>> getHandMelds(){
		return handMelds;
	}
	
	public ArrayList<Tile> getTilesForTableMelds(){
		return tilesForTableMelds;
	}
	
	public String displayHand() {
		StringBuilder sb = new StringBuilder(35);
		sb.append("[");
		for (int i = 0; i < hand.size(); i++) {
			if (i == (hand.size() - 1)) {
				sb.append(hand.get(i).toString());
			}
			else {
				sb.append(hand.get(i).toString() + ", ");
			}
		}
		sb.append("]");
		return sb.toString();
	}
	
	public int getCount() {
		return hand.size();
	}
	
	public void addTile(Tile t) {
		hand.add(t);
		sortTiles();
		determineHandMeldsAndTilesForTableMelds();
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
	
	private void sortTiles() {
		Collections.sort(hand, new TileComparator());
	}
	
	private void determineHandMeldsAndTilesForTableMelds() {
		HashMap<Integer, ArrayList<Tile>> handTiles = new HashMap<Integer, ArrayList<Tile>>(13);
		List<ArrayList<Tile>> validMelds = new ArrayList<ArrayList<Tile>>();
		ArrayList<Tile> remainingTiles = new ArrayList<Tile>();
		
		for(Tile tile : hand) { //Makes sure all Joker values are reset
			if(tile.isJoker()) {
				tile.setValue("-1");
			}
		}
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
					
					checkNextTileValue(tileValue+1, tileColour, handTiles, tempMeld);
					
					if (tempMeld.size() > 2) {
						//remove tiles in tempMeld from search space
						iter.remove(); // remove the current tile
						for (int i = 1; i < tempMeld.size(); ++i) { //remove the other tiles in the meld (excluding current tile)
							Tile t = tempMeld.get(i);
							if(!t.isJoker()) { 
								handTiles.get(t.getValue()).remove(t);	
							} else {
								handTiles.get(-1).remove(t);
							}
						}
						validMelds.add(tempMeld);
					}
				}
			}
		}
		
		//Resets Joker values so they can be used properly for sets
		if(handTiles.containsKey(-1)) {
			for(Tile joker : handTiles.get(-1)) {
				joker.setValue("-1");
			}
		}
		
		//find set melds
		for (int tileValue = 1; tileValue <= 13; ++tileValue) {
			if (handTiles.containsKey(tileValue) && handTiles.get(tileValue).size() >= 2) {
				ArrayList<String> colours = new ArrayList<String>();
				ArrayList<Tile> tempMeld = new ArrayList<Tile>();
				String[] validColors = {"R", "B", "G", "O"};
				
				for (Tile tile: handTiles.get(tileValue)) {	
					//get this tile's info
					String[] tileInfo = tile.getInfo();
					String tileColour = tileInfo[0];
					
					if (!colours.contains(tileColour)) {
						colours.add(tileColour);
						tempMeld.add(tile);
					}
				}
				
				//Checks if Jokers can be used to make a set
				if(tempMeld.size() <= 2 && handTiles.containsKey(-1)) {
					for(Tile tile: handTiles.get(-1)) { //Checks for Joker tiles
						for(String jokerColor : validColors) {
							if(!colours.contains(jokerColor)) { //If the color is not in the set then add it in and change the joker internal values
								tile.setColor(jokerColor);
								tile.setValue(Integer.toString(tileValue));
								tempMeld.add(tile);
								break;
							}
						}
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
		else if(handTiles.containsKey(-1)) { //If Joker
			ArrayList<Tile> nextTiles = handTiles.get(-1);
			//Color doesn't matter
			for (Tile tile: nextTiles) {
				if(!tempMeld.contains(tile)) { //If the joker is not already in the meld
					tempMeld.add(tile);
					tile.setValue(Integer.toString(nextTileValue)); //Sets the value of the Joker to what tile it's imitating 
					tile.setColor(tileColour);
					checkNextTileValue(nextTileValue+1, tileColour, handTiles, tempMeld); //Continue checking
					break;					
				}
			}
		}
	}
	
	public void playHandMeld(ArrayList<ArrayList<Tile>> tableTiles, boolean minimum) {
		if(minimum) {
			ArrayList<Integer> values = new ArrayList<Integer>();
			int sum = 0;
			//Gets the sum of all melds 
			for(ArrayList<Tile> meld : handMelds) {
				for(Tile tile : meld) {
					sum += tile.getValue();
				}
				values.add(sum);
				sum = 0;
			}
			
			int min = Integer.MAX_VALUE - 1;
			int value;
			ArrayList<Integer> minIndex = new ArrayList<Integer>();
			minIndex.add(-1);
			ArrayList<Integer> sub30 = new ArrayList<Integer>();
			ArrayList<Integer> sub30Index = new ArrayList<Integer>();
			//Find the minimum amongst all meld values
			for(int i = 0; i < values.size(); i++) {
				value = values.get(i);
				if(value >= 30) {
					if (value < min) {
						min = value;
						minIndex.set(0, i);
					}
				} else {
					sub30.add(value);
					sub30Index.add(i);
				}
			}
			
			//Check the combinations of melds less than 30
			//Reference: https://www.geeksforgeeks.org/finding-all-subsets-of-a-given-set-in-java/
			ArrayList<ArrayList<Integer>> combinations = new ArrayList<ArrayList<Integer>>();
			ArrayList<Integer> combo = new ArrayList<Integer>();
			int sub30PowerSetSize = (int)Math.pow(2,sub30.size());
			for(int i = 0; i < sub30PowerSetSize; i++) {
	            for (int j = 0; j < sub30.size(); j++) {
	            	 
	                if ((i & (1 << j)) > 0) {
	                	combo.add(j);
	                	
	                }

	            }
	            combinations.add(combo);
	            combo = new ArrayList<Integer>();
			}
			for(ArrayList<Integer> comboIndex : combinations) {
				sum = 0;
				for(int index : comboIndex) {
					sum += sub30.get(index);
				}
				if(sum >= 30 && sum < min) {
					min = sum;
					minIndex.clear();
					for(int index : comboIndex) {
						minIndex.add(sub30Index.get(index));
					}
				}
			}
			
			ArrayList<ArrayList<Tile>> melds = new ArrayList<ArrayList<Tile>>();
			ArrayList<Tile> meld;
			for(int index : minIndex) {
				meld = handMelds.get(index);
				melds.add(meld);
				tableTiles.add(meld);
			}
			
			for(ArrayList<Tile> rmMeld : melds) {
				for(Tile tile : rmMeld) {
					removeTile(tile);
				}
			}
			
		} else {
			//Remove all melds
			for(ArrayList<Tile> meld : handMelds) {
				tableTiles.add(meld);
				for(Tile tile : meld) {
					removeTile(tile);
				}
			}
			
			
		}
		
		sortTiles();
		determineHandMeldsAndTilesForTableMelds();
	}
	
	public void playTableMeld(ArrayList<ArrayList<Tile>> tableTiles) {
		for(int i = 0; i < tableTiles.size(); i++){
	        // The first melds in the table will be prioritized this way (having tiles added to them if possible)
	        ArrayList<Tile> looseTiles = getTilesForTableMelds();
	        ArrayList<Tile> temp;
	        ArrayList<Tile> meld;
	        
	        for (Tile t : looseTiles) {
	        	meld = tableTiles.get(i);
	        	temp = new ArrayList<>(meld);
	        	temp.add(0,t);
	            if (Meld.checkValidity(temp)) { // could successfully add a tile to front of meld and maintain validity
	                // is a valid meld
	                tableTiles.set(tableTiles.indexOf(meld), temp);
	                removeTile(t);
	                continue;
	            }
	            temp = new ArrayList<>(meld);
	            temp.add(meld.size(), t);
	            if (Meld.checkValidity(temp)) { // could successfully add to back of meld and maintain validity
	                // is a valid meld
	                tableTiles.set(tableTiles.indexOf(meld), temp);
	                removeTile(t);
	                continue;
	            }
	            // check if can be inserted within positions inside the meld and still be valid
                List<ArrayList<Tile>> splitMelds = getSplitMeld(meld, t);
                if (splitMelds == null) {
                    continue;
                }
                else {
                    tableTiles.set(tableTiles.indexOf(meld), splitMelds.get(0));
                    tableTiles.add(splitMelds.get(1));
                }
                
                removeTile(t);
	            
	        }
	    }
	}
	
	public void playTableMeld(ArrayList<ArrayList<Tile>> tableTiles, ArrayList<Tile> looseTiles) {
		for(int i = 0; i < tableTiles.size(); i++){
	        // The first melds in the table will be prioritized this way (having tiles added to them if possible)
	        ArrayList<Tile> temp;
	        ArrayList<Tile> meld;
	        
	        for (Tile t : looseTiles) {
	        	meld = tableTiles.get(i);
	        	temp = new ArrayList<>(meld);
	        	temp.add(0,t);
	            if (Meld.checkValidity(temp)) { // could successfully add a tile to front of meld and maintain validity
	                // is a valid meld
	                tableTiles.set(tableTiles.indexOf(meld), temp);
	                removeTile(t);
	                continue;
	            }
	            temp = new ArrayList<>(meld);
	            temp.add(meld.size(), t);
	            if (Meld.checkValidity(temp)) { // could successfully add to back of meld and maintain validity
	                // is a valid meld
	                tableTiles.set(tableTiles.indexOf(meld), temp);
	                removeTile(t);
	                continue;
	            }
	            // check if can be inserted within positions inside the meld and still be valid
                List<ArrayList<Tile>> splitMelds = getSplitMeld(meld, t);
                if (splitMelds == null) {
                    continue;
                }
                else {
                    tableTiles.set(tableTiles.indexOf(meld), splitMelds.get(0));
                    tableTiles.add(splitMelds.get(1));
                }
                
                removeTile(t);
	            
	        }
	    }
	}

	private ArrayList<ArrayList<Tile>> getSplitMeld(ArrayList<Tile> m, Tile t) {
	    if ((t.getValue() < m.get(0).getValue() - 1) || (t.getValue() > m.get(m.size() - 1).getValue() + 1)) {
	        return null;
	    }
	    // Make shallow copy for temp to not affect the original meld
	    ArrayList<Tile> temp = new ArrayList<Tile>(m);
	    temp.add(t);
	    Collections.sort(temp, new TileComparator());
	    int insert_index = temp.indexOf(t);
	    ArrayList<Tile> meld1 = new ArrayList<Tile>(m.subList(0, insert_index));
	    meld1.add(t);
	    ArrayList<Tile> meld2 = new ArrayList<Tile>(m.subList(insert_index, m.size()));
	    
	    if ((Meld.checkValidity(meld1)) && (Meld.checkValidity(meld2))) {
	        ArrayList<ArrayList<Tile>> nL = new ArrayList<ArrayList<Tile>>();
	        nL.add(meld1);
	        nL.add(meld2);   
	        return nL;
	    }
	    
	    return null;
	}
	    
}