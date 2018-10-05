package core;

import junit.framework.TestCase;

public class TileTesting extends TestCase{
	public void testGetInfo() {
		Tile t = new Tile("R", "13");
		Tile t1 = new Tile("B", "1");
		Tile t2 = new Tile("G", "7");
		Tile t3 = new Tile("O", "9");
		assertEquals(new String[] {"R", "13"}, t.getInfo());
		assertEquals(new String[] {"B", "1"}, t1.getInfo());
		assertEquals(new String[] {"G", "7"}, t2.getInfo());
		assertEquals(new String[] {"O", "9"}, t3.getInfo());
	}
}

