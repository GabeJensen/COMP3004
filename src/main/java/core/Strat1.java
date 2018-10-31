package core;

import java.util.ArrayList;
import java.util.List;

public class Strat1 implements PlayerStrategy {
	public int strat(Hand h, boolean initialMeld, List<ArrayList<Tile>> tableTiles, int min) {
		// Strategy 1.
		// Aggressively/hastily plays everything when possible.
		/*
		 * if (player.currentlyPlayingMeld.getValue >= 30 && !player.getInit30Flag) 
		 * 		play the meld
		 * 		player.playedInit30
		 * else
		 * 		if (player.getInit30Flag) // the player has played their initial 30: they will play as much per turn now
		 * 			play as much as possible
		 * 		else
		 * 			// player initial30 is false and the presented melds if any are not at least 30 value
		 */
		ArrayList<ArrayList<Tile>> handMelds = new ArrayList<ArrayList<Tile>>(h.getHandMelds());
		ArrayList<Tile> beforeTableTiles = new ArrayList<Tile>(h.getTilesForTableMelds());
		int sum = 0;
		
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
			h.playHandMeld((ArrayList)tableTiles, false);
			h.playTableMeld((ArrayList)tableTiles);
			if(h.getTilesForTableMelds().equals(beforeTableTiles) && h.getHandMelds().equals(handMelds)) {
				return 0;
			}
			return 1;
		}
		
		return 0;
		
	}
}
