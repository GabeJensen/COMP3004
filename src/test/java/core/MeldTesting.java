package core;

import java.util.ArrayList;

import junit.framework.TestCase;

public class MeldTesting extends TestCase {
	
	public void testValidMeld() {
		Meld meld1 = new Meld();
		ArrayList<Tile> tiles1 = new ArrayList<Tile>();
		
		Tile tOne1 = new Tile("R", "1");
		Tile tTwo1 = new Tile("R", "2");
		Tile tThree1 = new Tile("R", "3");
		
		tiles1.add(tOne1);
		tiles1.add(tTwo1);
		tiles1.add(tThree1);
		
		assertEquals(meld1.createMeld(tiles1), true);
		
		Meld meld2 = new Meld();
		ArrayList<Tile> tiles2 = new ArrayList<Tile>();
		
		Tile tOne2 = new Tile("R", "2");
		Tile tTwo2 = new Tile("G", "2");
		Tile tThree2 = new Tile("O", "2");
		
		tiles2.add(tOne2);
		tiles2.add(tTwo2);
		tiles2.add(tThree2);
		
		assertEquals(meld2.createMeld(tiles2), true);
	}
	
	public void testInvalidMelds() {
		Meld meld = new Meld();
		ArrayList<Tile> tiles = new ArrayList<Tile>();
		
		Tile tOne = new Tile("R", "5");
		Tile tTwo = new Tile("R", "2");
		Tile tThree = new Tile("O", "3");
		
		tiles.add(tOne);
		tiles.add(tTwo);
		tiles.add(tThree);
		
		assertEquals(meld.createMeld(tiles), false);
		
		// test meld < 2 tiles
		Meld meld2 = new Meld();
		ArrayList<Tile> tiles2 = new ArrayList<Tile>();
		
		Tile tOne2 = new Tile("O", "4");
		Tile tTwo2 = new Tile("B", "4");
		
		tiles2.add(tOne2);
		tiles2.add(tTwo2);
		
		assertEquals(meld2.createMeld(tiles2), false);
		
		// test a run meld > 4 tiles with one incorrect color
		Meld meld3 = new Meld();
		ArrayList<Tile> tiles3 = new ArrayList<Tile>();
		
		Tile tOne3 = new Tile("B", "4");
		Tile tTwo3 = new Tile("B", "5");
		Tile tThree3 = new Tile("B", "6");
		Tile tFour3 = new Tile("G", "7");
		
		tiles3.add(tOne3);
		tiles3.add(tTwo3);
		tiles3.add(tThree3);
		tiles3.add(tFour3);
		
		assertEquals(meld3.createMeld(tiles3), false);
		
		// test a set meld with a duplicate colors
		Meld meld4 = new Meld();
		ArrayList<Tile> tiles4 = new ArrayList<Tile>();
		
		Tile tOne4 = new Tile("R", "11");
		Tile tTwo4 = new Tile("G", "11");
		Tile tThree4 = new Tile("R", "11");
		
		tiles4.add(tOne4);
		tiles4.add(tTwo4);
		tiles4.add(tThree4);
		
		assertEquals(meld4.createMeld(tiles4), false);
		
	}
	
	public void testGetValue() {
		Meld meld = new Meld();
		Tile t = new Tile("B", "7");
		Tile t1 = new Tile("O", "8");
		Tile t2 = new Tile("G", "9");
		Tile t3 = new Tile("R", "10");
		
		ArrayList<Tile> tiles = new ArrayList<Tile>();
		
		tiles.add(t);
		tiles.add(t1);
		tiles.add(t2);
		tiles.add(t3);
		
		meld.createMeld(tiles);
		
		assertEquals(34, meld.getValue());
		
		Meld meld2 = new Meld();
		ArrayList<Tile> tiles2 = new ArrayList<Tile>();
		
		Tile tOne2 = new Tile("R", "2");
		Tile tTwo2 = new Tile("G", "2");
		Tile tThree2 = new Tile("O", "2");
		
		tiles2.add(tOne2);
		tiles2.add(tTwo2);
		tiles2.add(tThree2);
		
		meld2.createMeld(tiles2);
		
		assertEquals(6, meld2.getValue());
		
		Meld emptyMeld = new Meld();
		
		assertEquals(0, emptyMeld.getValue());
	}
}
