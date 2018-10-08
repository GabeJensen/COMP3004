package core;

import junit.framework.TestCase;

public class HandTesting extends TestCase{
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
	
	/* might not actually exist, this function
	 * since only end up playing tiles as melds anyways
	 * public void testRemoveTiles() {
		Hand h = new Hand();
		Tile t1 = new Tile("R", "6");
		Tile t2 = new Tile("O","6");
		Tile t3 = new Tile("B", "6");
		
		h.addTile(t1);
		h.addTile(t2);
		h.addTile(t3);
		// assertEquals()
		
		for (int i = 0; i < h.getCount(); i++) {
			h.removeTile();
		}
		// assert that the tile hand count is now 0 
		// assertEquals 
	}*/
	
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
		
		h.sortTiles();
		
		// assertEquals what will be the return of this h.getHandMelds call? string? object?
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
		
		assertEquals(hand.removeTile(0).getInfo(), t1.getInfo());
		
		assertEquals(hand.getCount(), 3);
		
		assertEquals(hand.removeTile(0).getInfo(), t2.getInfo());
		
		assertEquals(hand.getCount(), 2);
		
		assertEquals(hand.removeTile(1).getInfo(), t4.getInfo());
		
		assertEquals(hand.getCount(), 1);
		
		assertEquals(hand.removeTile(5), -1);
		
		assertEquals(hand.getCount(), 1);
		
		
	}
}
