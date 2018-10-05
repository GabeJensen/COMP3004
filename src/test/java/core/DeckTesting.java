package core;

import static org.junit.Assert.*;

import junit.framework.TestCase;

public class DeckTesting extends TestCase{
	public void testDealTile() {
		Deck d = new Deck();
		// The last card to be added from the for loops is O, 13.
		// The deck deals the cards from the last added, so we should expect O, 13 to be the card here since we don't shuffle upon creation.
		assertArrayEquals(new String[] {"O", "13"}, d.dealTile().getInfo());
	}
	
	public void testShuffleDeck() {
		Deck d = new Deck();
		Deck noshuffle = new Deck();
		d.shuffleDeck();
		// The following assert should rarely ever fail ... due to the nature of testing a randomize shuffle, maybe once in a while the first tile dealt could be the same.
		assertNotEquals(noshuffle.dealTile().getInfo(), d.dealTile().getInfo());
	}
}
