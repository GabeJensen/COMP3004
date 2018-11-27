package core;

import static org.junit.Assert.*;

import junit.framework.TestCase;

public class DeckTesting extends TestCase{
	public void testDealTile() {
		Deck d = new Deck();
		// The last card to be added from the for loops is O, 13.
		// The deck deals the cards from the last added, so we should expect O, 13 to be the card here since we don't shuffle upon creation.
		assertArrayEquals(new String[] {"O", "13"}, d.dealTile().getInfo());
		
		int currentTileCount = d.getTileCount();
		
		for (int x = 0; x < currentTileCount; x++) {
			d.dealTile();
		}
		
		// The deck should not be dealing anything if it has no more tiles.
		assertEquals(null, d.dealTile());
	}
	
	public void testShuffleDeck() {
		Deck d = new Deck();
		Deck noshuffle = new Deck();
		d.shuffleDeck();
		// The following assert should rarely ever fail ... due to the nature of testing a randomize shuffle, maybe once in a while the first tile dealt could be the same.
		assertNotEquals(noshuffle.dealTile().getInfo(), d.dealTile().getInfo());
	}
	
	public void testDrawSpecific() {
		Deck d = new Deck();
		
		Tile t = d.drawSpecific("R6");
		assertEquals(t.toString(), "R6");
		t = d.drawSpecific("R6");
		assertEquals(t.toString(), "R6");
		t = d.drawSpecific("R6");
		assertEquals(t, null);
		
		t = d.drawSpecific("G7");
		assertEquals(t.toString(), "G7");
	}
}
