package core;

import junit.framework.TestCase;

public class DeckTesting extends TestCase{
	public void testDealTile() {
		Deck d = new Deck();
		Tile t;
		// or assert that it should be the first card since it isn't shuffled.
		assertEquals(t, d.dealTile());
	}
	
	public void testShuffleDeck() {
		Deck d = new Deck();
		Deck noshuffle = new Deck();
		d.shuffleDeck();
		
		assertNotEquals(noshuffle, d);
	}
}
