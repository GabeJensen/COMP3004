package core;

import junit.framework.TestCase;

public class TileTesting extends TestCase{
	public void testGetValue() {
		Tile t = new Tile("R", "13");
		Tile t1 = new Tile("B", "1");
		Tile t2 = new Tile("G", "7");
		Tile t3 = new Tile("O", "9");
		assertEquals(13, t.getValue());
		assertEquals(1, t1.getValue());
		assertEquals(7, t2.getValue());
		assertEquals(9, t3.getValue());
	}
	
	public void testGetColor() {
		Tile t = new Tile("R", "13");
		Tile t1 = new Tile("B", "1");
		Tile t2 = new Tile("G", "7");
		Tile t3 = new Tile("O", "9");
		assertEquals("Red", t.getColor());
		assertEquals("Blue", t1.getColor());
		assertEquals("Green", t2.getColor());
		assertEquals("Orange", t3.getColor());
	}
}

