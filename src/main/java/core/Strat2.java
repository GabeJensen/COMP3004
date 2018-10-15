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
		return 0;
	}
}
