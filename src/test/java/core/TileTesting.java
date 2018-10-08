package core;

import junit.framework.TestCase;
import static org.junit.Assert.*;

public class TileTesting extends TestCase{
	public void testGetInfo() {
		Tile t = new Tile("R", "13");
		Tile t1 = new Tile("B", "1");
		Tile t2 = new Tile("G", "7");
		Tile t3 = new Tile("O", "9");
		assertArrayEquals(new String[] {"R", "13"}, t.getInfo());
		assertArrayEquals(new String[] {"B", "1"}, t1.getInfo());
		assertArrayEquals(new String[] {"G", "7"}, t2.getInfo());
		assertArrayEquals(new String[] {"O", "9"}, t3.getInfo());
	}
	
	public void testTileToString() {
		Tile t = new Tile("R", "13");
		Tile t1 = new Tile("B", "1");
		Tile t2 = new Tile("G", "7");
		Tile t3 = new Tile("O", "9");
		
		assertEquals("R13", t.toString());
		assertEquals("B1", t1.toString());
		assertEquals("G7", t2.toString());
		assertEquals("O9", t3.toString());
	}
	
	public void testTileEquals() {
		Tile t = new Tile("R", "13");
		Tile t1 = new Tile("B", "1");
		Tile t2 = new Tile("G", "7");
		Tile t3 = new Tile("O", "9");
		Tile dup = new Tile("R", "13");
		Tile dup1 = new Tile("B", "1");
		Tile dup2 = new Tile("G", "7");
		Tile dup3 = new Tile("O", "9");
		
		assertEquals(t, dup);
		assertEquals(t1, dup1);
		assertEquals(t2, dup2);
		assertEquals(t3, dup3);
		assertEquals(t, t);
		assertNotEquals(t, "not a tile");
	}
}

