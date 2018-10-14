package core;

import java.util.ArrayList;
import java.util.List;

public class Strat1 implements PlayerStrategy {
	public void strat(Hand h, boolean initialMeld, List<ArrayList<Tile>> tableTiles) {
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
	}
}
