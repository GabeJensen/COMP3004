package core;

import java.util.ArrayList;
import java.util.List;

public class Strat3 implements PlayerStrategy{
	public void strat(Hand h, boolean initialMeld, List<ArrayList<Tile>> tableTiles) {
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
	}
}
