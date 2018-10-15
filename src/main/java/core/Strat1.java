package core;

import java.util.ArrayList;
import java.util.List;

public class Strat1 implements PlayerStrategy {
	public int strat(Hand h, boolean initialMeld, List<ArrayList<Tile>> tableTiles) {
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
		ArrayList<Tile> largestMeld = new ArrayList<Tile>();
		List<ArrayList<Tile>> playerHand;
		ArrayList<Tile> playerHandList;
		playerHand = h.getHandMelds();
		ArrayList<Tile> tableMelds = new ArrayList<>();
		Hand tableHand = new Hand();
		Boolean tableMeldUsedNothing = true;
		
		if(playerHand.isEmpty()) {
			return 0;
		}
		if(!initialMeld) {
			
			while(!playerHand.isEmpty()) {
				for(int i = 0; i < playerHand.size(); i++) {
					if(largestMeld.isEmpty()) {
						largestMeld = playerHand.get(i);
					} else {
						if(Meld.getValue(largestMeld) < Meld.getValue(playerHand.get(i))) {
							largestMeld = playerHand.get(i);
						}
					}
				}
				
				tableTiles.add(largestMeld);
				playerHandList = h.getHand();
				
				for(int i = 0; i < largestMeld.size(); i++) {
					h.removeTile(playerHandList.indexOf(largestMeld.get(i)));
				}
				
				playerHand = h.getHandMelds();
				largestMeld = new ArrayList<Tile>();
			}
		} else {
			while(!playerHand.isEmpty()) {
//				tableMeldUsedNothing = false;
				for(int i = 0; i < playerHand.size(); i++) {
					if(largestMeld.isEmpty()) {
						largestMeld = playerHand.get(i);
					} else {
						if(Meld.getValue(largestMeld) < Meld.getValue(playerHand.get(i))) {
							largestMeld = playerHand.get(i);
						}
					}
				}
				
				tableTiles.add(largestMeld);
				playerHandList = h.getHand();
				
				for(int i = 0; i < largestMeld.size(); i++) {
					h.removeTile(playerHandList.indexOf(largestMeld.get(i)));
				}
				
				playerHand = h.getHandMelds();
				largestMeld = new ArrayList<Tile>();
			}
			
//			for(int j = 0; j < tableTiles.size(); j++) {
//				tableMelds.addAll(h.getHand());
//				tableMelds.addAll(tableTiles.get(j));
//				for (Tile tile : tableMelds) {
//					tableHand.addTile(tile);
//				}
//				playerHand = tableHand.getHandMelds();
//				if(!playerHand.isEmpty()) {
//					tableMeldUsedNothing = false;
//					for(int i = 0; i < playerHand.size(); i++) {
//						if(largestMeld.isEmpty()) {
//							largestMeld = playerHand.get(i);
//						} else {
//							if(Meld.getValue(largestMeld) < Meld.getValue(playerHand.get(i))) {
//								largestMeld = playerHand.get(i);
//							}
//						}
//					}
//					
//					tableTiles.set(j, largestMeld);
//				}
//				
//				tableHand = new Hand();
//				tableMelds = new ArrayList<Tile>();
//			}
			
//			if(tableMeldUsedNothing) {
//				return 0;
//			}
		}
		
		return 1;
	}
}
