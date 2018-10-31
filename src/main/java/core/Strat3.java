package core;

import java.util.ArrayList;
import java.util.List;

public class Strat3 implements PlayerStrategy{
	public int strat(Hand h, boolean initialMeld, List<ArrayList<Tile>> tableTiles, int min) {
		// Strategy 3
		// If the player has 3 or more cards than any other player, then it plays everything it can.
		// Otherwise, if it does not, then it just plays table melds.
		/*
		 * if !initialMeld
		 * 	if h.makeMeld.size > 30
		 * 		h.playMeld
		 * 	else
		 * 		draw card
		 * else if hand.endGame
		 * 	end the game
		 * else
		 * 	if table.minHandTotal + 3 <= h.totalCards
		 * 		play cards that require table melds
		 * 	else
		 * 		play as much as possible
		 * 	*/
		ArrayList<ArrayList<Tile>> handMelds = new ArrayList<ArrayList<Tile>>(h.getHandMelds());
		ArrayList<Tile> beforeTableTiles = new ArrayList<Tile>(h.getTilesForTableMelds());
		int sum = 0;
		
		//IF they only have melds then play them all
		if(beforeTableTiles.isEmpty()) {
			h.playHandMeld((ArrayList)tableTiles, false);
			return 1;
		}
		for(ArrayList<Tile> meld : handMelds) {
			for(Tile tile : meld) {
				sum += tile.getValue();
			}
		}
		
		if(!initialMeld) {
			if(sum >= 30) {
				h.playHandMeld((ArrayList)tableTiles, false);
				return 1;
			}
		} else {
			if(h.getCount() >= min + 3) {
				h.playTableMeld((ArrayList)tableTiles);
				h.playHandMeld((ArrayList)tableTiles, false);
				if(h.getTilesForTableMelds().equals(beforeTableTiles) && h.getHandMelds().equals(handMelds)) {
					return 0;
				}
			} else {
				h.playTableMeld((ArrayList)tableTiles);
				if(h.getTilesForTableMelds().equals(beforeTableTiles)) {
					return 0;
				}
			}
			return 1;
		}
		
		
		return 0;
	}
}
