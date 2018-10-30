package core;

import java.util.ArrayList;
import java.util.List;

public class Strat2 implements PlayerStrategy{
	public int strat(Hand h, boolean initialMeld, List<ArrayList<Tile>> tableTiles) {
		// Strategy 2
		// Plays table melds if it won't win. If it will win in the current turn, plays hand melds.
		/*
		 * if !initialMeld
		 * 	if tableTiles is not empty
		 * 		if h.makeMeld > 30
		 * 			play meld
		 * 	else
		 * 		draw card
		 * else if hand.endGame
		 *	play all cards left in hand
		 *else
		 *	play cards that require table melds 	
		 * */
		ArrayList<ArrayList<Tile>> handMelds = new ArrayList<ArrayList<Tile>>(h.getHandMelds());
		ArrayList<Tile> beforeTableTiles = new ArrayList<Tile>(h.getTilesForTableMelds());
		int sum = 0;
		
		for(ArrayList<Tile> meld : handMelds) {
			for(Tile tile : meld) {
				sum += tile.getValue();
			}
		}
		if(!initialMeld) {
			if(!tableTiles.isEmpty()) {
				if(beforeTableTiles.isEmpty()) {
					h.playHandMeld((ArrayList)tableTiles, false);
					return 1;
				} else if(sum >= 30) {
					h.playHandMeld((ArrayList)tableTiles, true);
					return 1;
				}
			}
			
		} else {
			if(beforeTableTiles.isEmpty()) {
				h.playHandMeld((ArrayList)tableTiles, false);
				return 1;
			}
			h.playTableMeld((ArrayList)tableTiles);
			if(!h.getTilesForTableMelds().equals(beforeTableTiles)) {
				return 1;
			}
		}
		return 0;
	}
}
