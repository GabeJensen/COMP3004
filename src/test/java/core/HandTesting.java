package core;

import static org.junit.Assert.assertArrayEquals;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class HandTesting extends TestCase{
	private void print(String text) {
		System.out.println(text);
	}
	
	public void testGetCount() {
		Hand h = new Hand();
		assertEquals(0, h.getCount());
	}
	
	public void testAddTiles() {
		Hand h = new Hand();
		Tile t1 = new Tile("R", "6");
		Tile t2 = new Tile("O","6");
		Tile t3 = new Tile("B", "6");
		
		h.addTile(t1);
		h.addTile(t2);
		h.addTile(t3);
		// assert the tile count of the hand is no longer 0?
		assertEquals(3, h.getCount());
	}
	
	public void testSortTiles() {
		Hand h = new Hand();
		Tile t1 = new Tile("R", "8");
		Tile t2 = new Tile("O","4");
		Tile t3 = new Tile("B", "2");
		Tile t4 = new Tile("G", "11");
				
		h.addTile(t1);
		h.addTile(t2);
		h.addTile(t3);
		h.addTile(t4);
		
		h.sortTiles();
		
		// how to assert the sort? 
		
		h.addTile(new Tile("R", "6"));
		
		h.sortTiles();
		
		// assert after adding a new tile
	}
	
	public void testDisplayHand() {
		/*
		 * This cannot be asserted if the for loop just prints.
		 */
		print("testDisplayHand start");
		Hand h = new Hand();
		Tile t1 = new Tile("R", "6");
		Tile t2 = new Tile("O","7");
		Tile t3 = new Tile("B", "8");
		Tile t4 = new Tile("G", "9");
		Tile t5 = new Tile("O", "12");
		Tile t6 = new Tile("B", "2");
				
		h.addTile(t1);
		h.addTile(t2);
		h.addTile(t3);
		h.addTile(t4);
		h.addTile(t5);
		h.addTile(t6);
		
		h.displayHand();
		
		h.sortTiles();
		print("...");
		
		h.displayHand();
		print("testDisplayHand end");
	}
	
	public void testGetHandMelds() {
		Hand h = new Hand();
		Tile t1 = new Tile("R", "6");
		Tile t2 = new Tile("O","7");
		Tile t3 = new Tile("B", "8");
		Tile t4 = new Tile("G", "9");
		Tile t5 = new Tile("O", "12");
		Tile t6 = new Tile("B", "2");
				
		h.addTile(t1);
		h.addTile(t2);
		h.addTile(t3);
		h.addTile(t4);
		h.addTile(t5);
		h.addTile(t6);
		
		// This would be more for display purposes and for the user only. Not needed for the computer players.
		h.sortTiles();
		
		List<ArrayList<Tile>> melds = new ArrayList<>();
		ArrayList<Tile> meld1 = new ArrayList<Tile>();
		meld1.add(t1);
		meld1.add(t2);
		meld1.add(t3);
		meld1.add(t4);
		melds.add(meld1);
		// In this case, we only should have the one meld.
		assertEquals(melds.get(0), h.getHandMelds().get(0));
		// will be returning R6,O7,B8,G9 in this case
	}
	
	public void testRemoveTile() {
		Hand hand = new Hand();
		
		Tile t1 = new Tile("R", "8");
		Tile t2 = new Tile("O","4");
		Tile t3 = new Tile("B", "2");
		Tile t4 = new Tile("G", "11");
				
		hand.addTile(t1);
		hand.addTile(t2);
		hand.addTile(t3);
		hand.addTile(t4);
		
		assertArrayEquals(hand.removeTile(0).getInfo(), t1.getInfo());
		
		assertEquals(hand.getCount(), 3);
		
		assertArrayEquals(hand.removeTile(0).getInfo(), t2.getInfo());
		
		assertEquals(hand.getCount(), 2);
		
		assertArrayEquals(hand.removeTile(1).getInfo(), t4.getInfo());
		
		assertEquals(hand.getCount(), 1);
		
		assertNull(hand.removeTile(5));
		
		assertEquals(hand.getCount(), 1);
		
		
	}
	
	public void testRemoveMultipleTiles() {
		Hand hand = new Hand();
		
		Tile t1 = new Tile("R", "8");
		Tile t2 = new Tile("O","4");
		Tile t3 = new Tile("B", "2");
		Tile t4 = new Tile("G", "11");
		Tile t5 = new Tile("G", "7");
		Tile t6 = new Tile("B", "5");
				
		hand.addTile(t1);
		hand.addTile(t2);
		hand.addTile(t3);
		hand.addTile(t4);
		hand.addTile(t5);
		hand.addTile(t6);
		
		ArrayList<Integer> indices = new ArrayList<>();
		
		indices.add(1);
		indices.add(3);
		indices.add(4);
		
		ArrayList<Tile> removedTiles = new ArrayList<>();
		
		removedTiles.add(t2);
		removedTiles.add(t4);
		removedTiles.add(t5);
		
		ArrayList<Tile> returnedTiles = hand.removeTiles(indices);
		
		for(int i = 0; i < removedTiles.size(); i++) {
			assertArrayEquals(returnedTiles.get(i).getInfo(), removedTiles.get(i).getInfo());
		}
	}
	
	public void testRemoveMultipleTilesInvalid() {
		Hand hand = new Hand();
		
		Tile t1 = new Tile("R", "8");
		Tile t2 = new Tile("O","4");
		Tile t3 = new Tile("B", "2");
		Tile t4 = new Tile("G", "11");
		Tile t5 = new Tile("G", "7");
		Tile t6 = new Tile("B", "5");
				
		hand.addTile(t1);
		hand.addTile(t2);
		hand.addTile(t3);
		hand.addTile(t4);
		hand.addTile(t5);
		hand.addTile(t6);
		
		ArrayList<Integer> indices = new ArrayList<>();
		
		indices.add(1);
		indices.add(3);
		indices.add(10);
		
		assertNull(hand.removeTiles(indices));
	}	
}
