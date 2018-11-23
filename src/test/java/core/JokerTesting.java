package core;

import static org.junit.Assert.assertArrayEquals;

import junit.framework.TestCase;

public class JokerTesting extends TestCase{
	public void testJokerTile() {
		Tile joker = new Tile("R", "J");
		Tile tile = new Tile("O", "1");
		
		//Test that the tile is a joker
		assertEquals(true, joker.isJoker());
		assertEquals(false, tile.isJoker());
		//Test Joker Value
		assertEquals(-1, joker.getValue());
	}
	
	
	public void testSetJoker() {
		Tile joker = new Tile("O", "J");
		//Test the set works with valid number
		assertEquals(true, joker.setValue("12"));
		//TEst still a joker
		assertEquals(true, joker.isJoker());
		//Test value
		assertEquals(12, joker.getValue());
		//Test it cant set Joker to invalid number
		assertEquals(false, joker.setValue("50"));
	}
	
	public void testJokerString() {
		Tile joker = new Tile("B", "J");
		
		//Test it still returns a string as a joker
		assertEquals("BJ", joker.toString());
		assertArrayEquals(new String[] {"B",  "J"}, joker.getInfo());
		
		joker.setValue("5");
		//Checking that the string doesn't change when changing the value
		assertEquals("BJ", joker.toString());
		assertArrayEquals(new String[] {"B",  "J"}, joker.getInfo());
	}
}
