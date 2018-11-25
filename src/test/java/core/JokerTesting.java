package core;

import static org.junit.Assert.assertArrayEquals;

import java.util.ArrayList;
import java.util.List;

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
		//Works for integers
		assertTrue(joker.setValue(10));
		assertEquals(10, joker.getValue());
		
	}
	
	public void testSetColorJoker() {
		//Color would be needed to be changed for sets so it can properly replicate a tile
		Tile joker = new Tile("O", "J");
		Tile t = new Tile("R", "5");
		//Works with valid color
		assertEquals(true, joker.setColor("B"));
		//Color properly changed
		assertEquals("B", joker.getInfo()[0]);
		//Can't set itself to colors that dont exist
		assertEquals(false, joker.setColor("X"));
		//Regular tile cant set color
		assertEquals(false, t.setColor("O"));
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
	
	public void testJokerEqual() {
		Tile j1 = new Tile("R", "J");
		Tile j2 = new Tile("O", "J");
		//Jokers are of equal value regardless of value and color
		assertEquals(j1, j2);
		assertTrue(j1.equals(j2));
		
		j1.setValue("12");
		j2.setValue("1");
		
		assertEquals(j1, j2);
		
		Tile t1 = new Tile("R", "12");
		Tile t2 = new Tile("O", "1");
		//Jokers are always less than other tiles
		assertNotSame(t1,j1);
		assertNotSame(j2, t2);
	}
	
	public void testJokerInHand() {
		Hand h1 = new Hand();
		List<ArrayList<Tile>> melds = new ArrayList<ArrayList<Tile>>();
		Tile t1 = new Tile("R", "6");
		Tile t2 = new Tile("R", "7");
		Tile joker = new Tile("R", "J");
		
		h1.addTile(t1);
		h1.addTile(t2);
		h1.addTile(joker);
		melds.add(h1.getHand());
		
		ArrayList<ArrayList<Tile>> m1 = (ArrayList)h1.getHandMelds();
		
		//Checking if the Joker is apart of the run
		assertEquals(melds, m1);
		assertEquals(8, joker.getValue());
		
		//Clear this arrayList to use it again from scratch
		melds.clear();
		
		Hand h2 = new Hand();
		Tile t3 = new Tile("B", "8");
		Tile t4 = new Tile("O", "8");
		Tile joker2 = new Tile("B", "J");
		
		h2.addTile(t3);
		h2.addTile(t4);
		h2.addTile(joker2);
		melds.add(h2.getHand());
		
		ArrayList<ArrayList<Tile>> m2 = (ArrayList)h2.getHandMelds();
		//Checking if the Joker is apart of the set
		assertEquals(melds, m2);
		assertEquals(8, joker2.getValue());
		
		melds.clear();
		//Testing adding a Joker in the middle of a run
		Hand h3 = new Hand();
		Tile t5 = new Tile("O", "6");
		Tile t6 = new Tile("O", "7");
		Tile joker3 = new Tile("R", "J");
		Tile t7 = new Tile("O", "9");
		
		h3.addTile(t7);
		h3.addTile(joker3);
		h3.addTile(t6);
		h3.addTile(t5);
		
		ArrayList<Tile> meld = new ArrayList<Tile>();
		//Joker gets added as a middle element
		meld.add(t5);
		meld.add(t6);
		meld.add(joker3);
		meld.add(t7);
		melds.add(meld);
		
		ArrayList<ArrayList<Tile>> m3 = (ArrayList)h3.getHandMelds();
		//Checking if the Joker is apart of the set
		assertEquals(melds, m3);
	}
	
	public void testMeldValidityWithJoker() {
		//Testing runs
		//First meld tiles. Adding to end
		Tile tr1 = new Tile("R", "10");
		Tile tr2 = new Tile("R", "11");
		Tile tr3 = new Tile("R", "12");
		Tile jokerR1 = new Tile("O", "J");
		//Second meld tiles. Adding to beginning
		Tile jokerR2 = new Tile("B", "J");
		Tile tr4 = new Tile("O", "2");
		Tile tr5 = new Tile("O", "3");
		Tile tr6 = new Tile("O", "4");
		//Third meld tiles. Adding to middle
		Tile tr7 = new Tile("B", "6");
		Tile tr8 = new Tile("B", "7");
		Tile jokerR3 = new Tile("B", "J");
		Tile tr9 = new Tile("B", "9");
		Tile tr10 = new Tile("B", "10");
		//Fourth meld. 2 Jokers in run
		Tile jokerR4 = new Tile("B", "J");
		Tile jokerR5 = new Tile("O", "J");
		Tile tr12 = new Tile("B", "5");
		Tile tr13 = new Tile("B", "6");
		Tile[] meldr1 = {tr1, tr2, tr3, jokerR1};
		Tile[] meldr2 = {jokerR2, tr4, tr5, tr6};
		Tile[] meldr3 = {tr7, tr8, jokerR3, tr9, tr10};
		Tile[] meldr4 = {jokerR4, jokerR5, tr12, tr13};
		
		ArrayList<Tile> mr1 = new ArrayList<Tile>();
		ArrayList<Tile> mr2 = new ArrayList<Tile>();
		ArrayList<Tile> mr3 = new ArrayList<Tile>();
		ArrayList<Tile> mr4 = new ArrayList<Tile>();
		
		for(Tile tile : meldr1) {
			mr1.add(tile);
		}
		
		for(Tile tile : meldr2) {
			mr2.add(tile);
		}
		
		for(Tile tile : meldr3) {
			mr3.add(tile);
		}
		
		for(Tile tile : meldr4) {
			mr4.add(tile);
		}
		
		//Asserting that they are valid melds
		assertTrue(Meld.checkValidity(mr1));
		assertTrue(Meld.checkValidity(mr2));
		assertTrue(Meld.checkValidity(mr3));
		assertTrue(Meld.checkValidity(mr4));
		
		//Testing sets
		//First set meld. 4 tiles
		Tile joker4 = new Tile("O", "J");
		Tile ts1 = new Tile("R", "9");
		Tile ts2 = new Tile("G", "9");
		Tile ts3 = new Tile("O", "9");
		//Second set meld. 3 tiles
		Tile joker5 = new Tile("B", "J");
		Tile ts4 = new Tile("B", "5");
		Tile ts5 = new Tile("G", "5");
		//Third set meld. 2 jokers
		Tile joker6 = new Tile("B", "J");
		Tile joker7 = new Tile("R", "J");
		Tile ts6 = new Tile("R", "12");
		
		Tile[] melds1 = {ts1, joker4, ts2, ts3};
		Tile[] melds2 = {joker5, ts4, ts5};
		Tile[] melds3 = {joker6, joker7, ts6};
		
		ArrayList<Tile> ms1 = new ArrayList<Tile>();
		ArrayList<Tile> ms2 = new ArrayList<Tile>();
		ArrayList<Tile> ms3 = new ArrayList<Tile>();
		
		for(Tile tile : melds1) {
			ms1.add(tile);
		}
		
		for(Tile tile : melds2) {
			ms2.add(tile);
		}
		
		for(Tile tile : melds3) {
			ms3.add(tile);
		}
		
		assertTrue(Meld.checkValidity(ms1));
		assertTrue(Meld.checkValidity(ms2));
		assertTrue(Meld.checkValidity(ms3));
	}
	
	public void testMeldValiditywithJokerAndInvalidMelds() {
		//Testing runs
		//First meld tiles. Checking doesnt add above 14
		Tile tr1 = new Tile("R", "11");
		Tile tr2 = new Tile("R", "12");
		Tile tr3 = new Tile("R", "13");
		Tile jokerR1 = new Tile("O", "J");
		//Second meld tiles. Checking doesnt go below 1
		Tile jokerR2 = new Tile("B", "J");
		Tile tr4 = new Tile("O", "1");
		Tile tr5 = new Tile("O", "2");
		Tile tr6 = new Tile("O", "3");
		//Third meld tiles. Jokers need to be added in order
		Tile tr7 = new Tile("B", "6");
		Tile tr8 = new Tile("B", "7");
		Tile tr9 = new Tile("B", "9");
		Tile jokerR3 = new Tile("B", "J");
		Tile tr10 = new Tile("B", "10");
		//Fourth meld. 2 Jokers that shouldnt go below 1
		Tile jokerR4 = new Tile("B", "J");
		Tile jokerR5 = new Tile("O", "J");
		Tile tr12 = new Tile("B", "2");
		Tile tr13 = new Tile("B", "3");
		
		//Fifth run meld. Run doesnt go larger that 13 even with Joker
		Tile tr14 = new Tile("B", "1");
		Tile tr15 = new Tile("B", "2");
		Tile tr16 = new Tile("B", "3");
		Tile tr17 = new Tile("B", "4");
		Tile tr18 = new Tile("B", "5");
		Tile tr19 = new Tile("B", "6");
		Tile tr20 = new Tile("B", "7");
		Tile tr21 = new Tile("B", "8");
		Tile tr22 = new Tile("B", "9");
		Tile tr23 = new Tile("B", "10");
		Tile tr24 = new Tile("B", "11");
		Tile tr25 = new Tile("B", "12");
		Tile tr26 = new Tile("B", "13");
		Tile jokerR6= new Tile("G", "J");
		
		Tile[] meldr1 = {tr1, tr2, tr3, jokerR1};
		Tile[] meldr2 = {jokerR2, tr4, tr5, tr6};
		Tile[] meldr3 = {tr7, tr8, tr9, jokerR3, tr10};
		Tile[] meldr4 = {jokerR4, jokerR5, tr12, tr13};
		Tile[] meldr5 = {tr14, tr15, tr16, tr17, tr18, tr19, tr20, tr21, tr22, tr23, tr24, tr25, tr26, jokerR6};
		
		ArrayList<Tile> mr1 = new ArrayList<Tile>();
		ArrayList<Tile> mr2 = new ArrayList<Tile>();
		ArrayList<Tile> mr3 = new ArrayList<Tile>();
		ArrayList<Tile> mr4 = new ArrayList<Tile>();
		ArrayList<Tile> mr5 = new ArrayList<Tile>();
		
		for(Tile tile : meldr1) {
			mr1.add(tile);
		}
		
		for(Tile tile : meldr2) {
			mr2.add(tile);
		}
		
		for(Tile tile : meldr3) {
			mr3.add(tile);
		}
		
		for(Tile tile : meldr4) {
			mr4.add(tile);
		}
		
		for(Tile tile : meldr5) {
			mr5.add(tile);
		}
		//Asserting that they are valid melds
		assertFalse(Meld.checkValidity(mr1));
		assertFalse(Meld.checkValidity(mr2));
		assertFalse(Meld.checkValidity(mr3));
		assertFalse(Meld.checkValidity(mr4));
		assertFalse(Meld.checkValidity(mr5));
		
		//Testing sets
		//First set meld. Testing that it doesnt add in joker with a full set
		Tile joker4 = new Tile("O", "J");
		Tile ts1 = new Tile("R", "9");
		Tile ts2 = new Tile("G", "9");
		Tile ts3 = new Tile("O", "9");
		Tile ts4 = new Tile("B", "9");
		
		Tile[] melds1 = {ts1, joker4, ts2, ts3, ts4};
		
		ArrayList<Tile> ms1 = new ArrayList<Tile>();
		
		for(Tile tile : melds1) {
			ms1.add(tile);
		}
		
		assertFalse(Meld.checkValidity(ms1));
	}
}