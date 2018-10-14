package core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Hand {
	private ArrayList<Tile> hand;
	
	public Hand() {
		hand = new ArrayList<Tile>();
	}
	
	public List<ArrayList<Tile>> getHandMelds() {
		List<ArrayList<Tile>> userMelds = new ArrayList<ArrayList<Tile>>();
		List<ArrayList<Tile>> validMelds = new ArrayList<ArrayList<Tile>>();
		for (int meldSizes = 3; meldSizes <= hand.size(); meldSizes++) {
			userMelds.addAll(getCombination(hand, meldSizes));
		}
		for (ArrayList<Tile> potentialMeld : userMelds) {
			Collections.sort(potentialMeld, new TileComparator());
			if (Meld.checkValidity(potentialMeld)) {
				validMelds.add(potentialMeld);
			}
		}
		return validMelds;
	}
	
	private List<ArrayList<Tile>> getCombination(ArrayList<Tile> inHand, int r) {
		// r being the number of elements being chosen for the combinations (nCr)
		List<ArrayList<Tile>> kMelds = new ArrayList<ArrayList<Tile>>();
		int[] inputIndex = new int[r];
		
		/*
		 * The following code was adapted from https://stackoverflow.com/questions/29910312/
		 */
		
		if (r <= inHand.size()) {
		    for (int i = 0; (inputIndex[i] = i) < r - 1; i++);  
		    kMelds.add(new ArrayList<Tile>(Arrays.asList(getSubset(inHand, inputIndex))));
		    for(;;) {
		        int i;
		        for (i = r - 1; i >= 0 && inputIndex[i] == inHand.size() - r + i; i--); 
		        if (i < 0) {
		            break;
		        }
		        inputIndex[i]++;
		        for (++i; i < r; i++) {
		        	inputIndex[i] = inputIndex[i - 1] + 1; 
		        }
		        kMelds.add(new ArrayList<Tile>(Arrays.asList(getSubset(inHand, inputIndex))));
		    }
		}
		
		/*
		 * 
		 */
		return kMelds;
	}
	
	/*
	 * The following code was adapted from https://stackoverflow.com/questions/29910312/
	 */
	private Tile[] getSubset(ArrayList<Tile> input, int[] subset) {
	    Tile[] result = new Tile[subset.length]; 
	    for (int i = 0; i < subset.length; i++) 
	        result[i] = input.get(subset[i]);
	    return result;
	}
	
	/*
	 * 
	 */
	
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