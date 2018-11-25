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
}