package core;

import java.util.ArrayList;

import junit.framework.TestCase;

public class MeldTesting extends TestCase {
	
	public void testValidMeld() {
		ArrayList<Tile> tiles1 = new ArrayList<Tile>();
		
		Tile tOne1 = new Tile("R", "1");
		Tile tTwo1 = new Tile("R", "2");
		Tile tThree1 = new Tile("R", "3");
		
		tiles1.add(tOne1);
		tiles1.add(tTwo1);
		tiles1.add(tThree1);
		
		assertEquals(Meld.checkValidity(tiles1), true);
		
		ArrayList<Tile> tiles2 = new ArrayList<Tile>();
		
		Tile tOne2 = new Tile("R", "2");
		Tile tTwo2 = new Tile("G", "2");
		Tile tThree2 = new Tile("O", "2");
		
		tiles2.add(tOne2);
		tiles2.add(tTwo2);
		tiles2.add(tThree2);
		
		assertEquals(Meld.checkValidity(tiles2), true);
	}
	
	public void testInvalidMelds() {
		ArrayList<Tile> tiles = new ArrayList<Tile>();
		
		Tile tOne = new Tile("R", "5");
		Tile tTwo = new Tile("R", "2");
		Tile tThree = new Tile("O", "3");
		
		tiles.add(tOne);
		tiles.add(tTwo);
		tiles.add(tThree);
		
		assertEquals(Meld.checkValidity(tiles), false);
		
		// test meld < 2 tiles
		ArrayList<Tile> tiles2 = new ArrayList<Tile>();
		
		Tile tOne2 = new Tile("O", "4");
		Tile tTwo2 = new Tile("B", "4");
		
		tiles2.add(tOne2);
		tiles2.add(tTwo2);
		
		assertEquals(Meld.checkValidity(tiles2), false);
		
		// test a run meld > 4 tiles with one incorrect color
		ArrayList<Tile> tiles3 = new ArrayList<Tile>();
		
		Tile tOne3 = new Tile("B", "4");
		Tile tTwo3 = new Tile("B", "5");
		Tile tThree3 = new Tile("B", "6");
		Tile tFour3 = new Tile("G", "7");
		
		tiles3.add(tOne3);
		tiles3.add(tTwo3);
		tiles3.add(tThree3);
		tiles3.add(tFour3);
		
		assertEquals(Meld.checkValidity(tiles3), false);
		
		// test a set meld with a duplicate colors
		ArrayList<Tile> tiles4 = new ArrayList<Tile>();
		
		Tile tOne4 = new Tile("R", "11");
		Tile tTwo4 = new Tile("G", "11");
		Tile tThree4 = new Tile("R", "11");
		
		tiles4.add(tOne4);
		tiles4.add(tTwo4);
		tiles4.add(tThree4);
		
		assertEquals(Meld.checkValidity(tiles4), false);
		
	}
	
	public void testGetValue() {
		Tile t = new Tile("B", "7");
		Tile t1 = new Tile("O", "8");
		Tile t2 = new Tile("G", "9");
		Tile t3 = new Tile("R", "10");
		
		ArrayList<Tile> tiles = new ArrayList<Tile>();
		
		tiles.add(t);
		tiles.add(t1);
		tiles.add(t2);
		tiles.add(t3);
		
		assertEquals(34, Meld.getValue(tiles));
		
		Meld meld2 = new Meld();
		ArrayList<Tile> tiles2 = new ArrayList<Tile>();
		
		Tile tOne2 = new Tile("R", "2");
		Tile tTwo2 = new Tile("G", "2");
		Tile tThree2 = new Tile("O", "2");
		
		tiles2.add(tOne2);
		tiles2.add(tTwo2);
		tiles2.add(tThree2);

		assertEquals(6, Meld.getValue(tiles2));
		
		assertEquals(0, Meld.getValue(new ArrayList<Tile>()));
	}
	
	public void testInvalidRunOrder() {
		ArrayList<Tile> tiles = new ArrayList<Tile>();
		
		Tile tOne = new Tile("R", "3");
		Tile tTwo = new Tile("R", "1");
		Tile tThree = new Tile("R", "2");
		
		tiles.add(tOne);
		tiles.add(tTwo);
		tiles.add(tThree);
		
		assertEquals(Meld.checkValidity(tiles), false);
	}
}
