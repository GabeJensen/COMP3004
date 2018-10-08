package core;

import java.util.ArrayList;

import junit.framework.TestCase;

public class MeldTesting extends TestCase {
	
	public void testValidMeld() {
		Meld meld1 = new Meld();
		ArrayList<Tile> tiles1 = new ArrayList();
		
		Tile tOne1 = new Tile("R", "1");
		Tile tTwo1 = new Tile("R", "2");
		Tile tThree1 = new Tile("R", "3");
		
		tiles1.add(tOne1);
		tiles1.add(tTwo1);
		tiles1.add(tThree1);
		
		assertEquals(meld1.createMeld(), true);
		
		Meld meld2 = new Meld();
		ArrayList<Tile> tiles2 = new ArrayList();
		
		Tile tOne2 = new Tile("R", "2");
		Tile tTwo2 = new Tile("G", "2");
		Tile tThree2 = new Tile("O", "2");
		
		tiles2.add(tOne2);
		tiles2.add(tTwo2);
		tiles2.add(tThree2);
		
		assertEquals(meld2.createMeld(), true);
		
		Meld meld3 = new Meld();
		ArrayList<Tile> tiles3 = new ArrayList();
		
		Tile tOne3 = new Tile("R", "5");
		Tile tTwo3 = new Tile("R", "2");
		Tile tThree3 = new Tile("O", "3");
		
		tiles3.add(tOne3);
		tiles3.add(tTwo3);
		tiles3.add(tThree3);
		
		assertEquals(meld3.createMeld(), false);
	}
}
