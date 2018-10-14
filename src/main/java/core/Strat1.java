package core;

import java.util.ArrayList;
import java.util.List;

public class Strat1 implements PlayerStrategy {
	private Hand hand;
	private boolean init30; // The flag if a player has played their initial 30
	private List<ArrayList<Tile>> table;
	
	/*public Strat1(Hand h, boolean iFlag, List<ArrayList<Tile>> t) {
		this.hand = h;
		this.init30 = iFlag;
		this.table = t;
	}*/
	
	public void strat() {
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
