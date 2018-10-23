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
		h.addTile(t4);
		h.addTile(t2);
		h.addTile(t3);
	
		Hand h2 = new Hand();
		
		h2.addTile(t3);
		h2.addTile(t4);
		h2.addTile(t1);
		h2.addTile(t2);
		
		//h.displayHand();
		//h2.displayHand();
		// This would not be equal if they were not sorted.
		assertEquals(h.getHand(), h2.getHand());
		
		/*
		 * Neither of these asserts would work because nonEqualHand is not in the correct order.
		ArrayList<Tile> nonEqualHand = new ArrayList<Tile>();
		nonEqualHand.add(t1);
		nonEqualHand.add(t4);
		nonEqualHand.add(t2);
		nonEqualHand.add(t3);
		
		assertEquals(h.getHand(), nonEqualHand);
		assertEquals(h2.getHand(), nonEqualHand);
		*/
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
		print("testDisplayHand end");
	}
	
	public void testGetHandMelds() {
		Hand h = new Hand();
		List<ArrayList<Tile>> melds = new ArrayList<>();
		
		Tile t1 = new Tile("R", "6");
		Tile t2 = new Tile("R","7");
		Tile t3 = new Tile("R", "8");
		Tile t4 = new Tile("R", "9");
		Tile t5 = new Tile("O", "12");
		Tile t6 = new Tile("B", "2");
		Tile t7 = new Tile("R", "12");
		Tile t8 = new Tile("G", "12");
		Tile t9 = new Tile("B", "4");
		Tile t10 = new Tile("B", "5");
		Tile t11 = new Tile("B", "6");
		Tile t12 = new Tile("R", "6");
				
		h.addTile(t1);
		h.addTile(t2);
		h.addTile(t3);
		h.addTile(t4);
		h.addTile(t5);
		h.addTile(t6);
		h.addTile(t7);
		h.addTile(t8);
		h.addTile(t9);
		h.addTile(t10);
		h.addTile(t11);
		h.addTile(t12);
		
		ArrayList<Tile> meld1 = new ArrayList<Tile>();
		// B4, B5, B6
		meld1.add(t9);
		meld1.add(t10);
		meld1.add(t11);
		melds.add(meld1);

		ArrayList<Tile> meld2 = new ArrayList<Tile>();
		// R6, R7, R8, R9
		meld2.add(t1);
		meld2.add(t2);
		meld2.add(t3);
		meld2.add(t4);
		melds.add(meld2);
		
		ArrayList<Tile> meld3 = new ArrayList<Tile>();
		// R12, G12, O12
		meld3.add(t7); // Red first
		meld3.add(t8); // Then green
		meld3.add(t5); // Then orange
		melds.add(meld3);
		
		assertEquals(melds, h.getHandMelds());
	}
	
	public void testRemoveTile() {
		Hand hand = new Hand();
		
		Tile t1 = new Tile("R", "8");
		Tile t2 = new Tile("B", "2");
		Tile t3 = new Tile("G", "11");
		Tile t4 = new Tile("O","4");
				
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
		Tile t2 = new Tile("B", "2");
		Tile t3 = new Tile("B", "5");
		Tile t4 = new Tile("G", "7");
		Tile t5 = new Tile("G", "11");
		Tile t6 = new Tile("O","4");
				
		hand.addTile(t1);
		hand.addTile(t2);
		hand.addTile(t3);
		hand.addTile(t4);
		hand.addTile(t5);
		hand.addTile(t6);
		
		ArrayList<Integer> indices = new ArrayList<Integer>();
		
		indices.add(1);
		indices.add(3);
		indices.add(4);
		
		ArrayList<Tile> removedTiles = new ArrayList<Tile>();
		
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

	public void testRemoveTileByTileInfo() {
		Hand hand = new Hand();
		
		Tile t1 = new Tile("R", "8");
		Tile t2 = new Tile("O","4");
		Tile t3 = new Tile("B", "2");
		Tile t4 = new Tile("G", "11");
		Tile t5 = new Tile("G", "7");
		Tile t6 = new Tile("B", "5");
		Tile t7 = new Tile("B", "5");
		Tile t8 = new Tile("O", "1");
		
		hand.addTile(t1);
		hand.addTile(t2);
		hand.addTile(t3);
		hand.addTile(t4);
		hand.addTile(t5);
		hand.addTile(t6);
		
		assertArrayEquals(hand.removeTile(t1).getInfo(), t1.getInfo());
		
		assertEquals(hand.getCount(), 5);
		
		//Can remove tiles that aren't explicitly in the hand but are of the same value
		assertArrayEquals(hand.removeTile(t7).getInfo(), t6.getInfo());
		
		assertNull(hand.removeTile(t8));
	}
	
	public void testPlayTableMeld() {    
	    /*
	    Table melds:
	    [R3 R4 R5], [O4 O5 O6 O7 O8]
	    PLayer hand:
	    [R2 R6 O6]
	    */
	    
	    ArrayList<ArrayList<Tile>> table = new ArrayList<ArrayList<Tile>>();
	    ArrayList<Tile> meld1 = new ArrayList<Tile>();
	    ArrayList<Tile> meld2 = new ArrayList<Tile>();
	    
	    Tile t1 = new Tile("R", "3");
	    Tile t2 = new Tile("R","4");
	    Tile t3 = new Tile("R", "5");
	    
	    meld1.add(t1);
	    meld1.add(t2);
	    meld1.add(t3);
	    
	    Tile t4 = new Tile("O", "4");
	    Tile t5 = new Tile("O", "5");
	    Tile t6 = new Tile("O", "6");
	    Tile t7 = new Tile("O", "7");
	    Tile t8 = new Tile("O", "8");
	    
	    meld2.add(t4);
	    meld2.add(t5);
	    meld2.add(t6);
	    meld2.add(t7);
	    meld2.add(t8); 
	    
	    table.add(meld1);
	    table.add(meld2);
	    
	    Hand h = new Hand();
	            
	    Tile t9 = new Tile("R", "2");
	    Tile t10 = new Tile("R","6");
	    Tile t11 = new Tile("O", "6");

	    h.addTile(t9);
	    h.addTile(t10);
	    h.addTile(t11);

	    h.playTableMeld(table);
	    
	    ArrayList<Tile> correctMeld1 = new ArrayList<Tile>();
	    
	    correctMeld1.add(t9);
	    correctMeld1.add(t1);
	    correctMeld1.add(t2);
	    correctMeld1.add(t3);
	    correctMeld1.add(t10);
	    
	    ArrayList<Tile> correctMeld2 = new ArrayList<Tile>();
	    
	    correctMeld2.add(t4);
	    correctMeld2.add(t5);
	    correctMeld2.add(t11);
	    
	    ArrayList<Tile> correctMeld3 = new ArrayList<Tile>();
	    
	    correctMeld3.add(t6);
	    correctMeld3.add(t7);
	    correctMeld3.add(t8);
	    
	    assertEquals(table.get(0), correctMeld1);
	    assertEquals(table.get(1), correctMeld2);
	    assertEquals(table.get(2), correctMeld3);
	    
	    assertEquals(0, h.getCount());
	    
	    Tile t12 = new Tile("B", "2");
	    h.addTile(t12);
	    h.playTableMeld(table);
	    
	    assertEquals(1, h.getCount());
	}
	
	public void testPlayHandMeld() {
		ArrayList<ArrayList<Tile>> table = new ArrayList<ArrayList<Tile>>();
	    ArrayList<Tile> meld1 = new ArrayList<Tile>();
	    ArrayList<Tile> meld2 = new ArrayList<Tile>();
	    ArrayList<Tile> remainingTiles = new ArrayList<Tile>();
	    Hand h = new Hand();
	    
	    Tile t1 = new Tile("R", "3");
	    Tile t2 = new Tile("G","3");
	    Tile t3 = new Tile("B", "3");
	    
	    meld1.add(t1);
	    meld1.add(t2);
	    meld1.add(t3);
	    h.addTile(t1);
	    h.addTile(t2);
	    h.addTile(t3);
	    
	    Tile t4 = new Tile("O", "4");
	    Tile t5 = new Tile("O", "5");
	    Tile t6 = new Tile("O", "6");
	    Tile t7 = new Tile("O", "7");
	    Tile t8 = new Tile("O", "8");
	    
	    meld2.add(t4);
	    meld2.add(t5);
	    meld2.add(t6);
	    meld2.add(t7);
	    meld2.add(t8); 
	    h.addTile(t4);
	    h.addTile(t5);
	    h.addTile(t6);
	    h.addTile(t7);
	    h.addTile(t8);
	            
	    Tile t9 = new Tile("B", "2");
	    Tile t10 = new Tile("G","6");
	    Tile t11 = new Tile("O", "12");

	    remainingTiles.add(t9);
	    remainingTiles.add(t10);
	    remainingTiles.add(t11);
	    h.addTile(t9);
	    h.addTile(t10);
	    h.addTile(t11);

	    //True argument means play the minimum amount of tiles to reach 30
	    h.playHandMeld(table, true);
	    
	    ArrayList<Tile> firstPlayTiles = new ArrayList<Tile>(meld1);
	    assertEquals(table.get(0), meld2);
	    assertEquals(h.getHand(), firstPlayTiles.addAll(remainingTiles));
	    
	    assertEquals(6, h.getCount());
	    
	    //False argument means play what you have remaining
	    h.playHandMeld(table, false);
	    
	    assertEquals(table.get(1), meld1);
	    assertEquals(h.getHand(), remainingTiles);
	    assertEquals(3, h.getCount());
	    
	    Tile t12 = new Tile("B", "13");
	    h.addTile(t12);
	    h.playHandMeld(table, false);
	    
	    assertEquals(4, h.getCount());
	}
	
	public void testPlayHandMeldMinimum() {
		ArrayList<ArrayList<Tile>> table = new ArrayList<ArrayList<Tile>>();
	    ArrayList<Tile> meld1 = new ArrayList<Tile>();
	    ArrayList<Tile> meld2 = new ArrayList<Tile>();
	    ArrayList<Tile> remainingTiles = new ArrayList<Tile>();
	    Hand h = new Hand();
	    
	    Tile t1 = new Tile("R", "9");
	    Tile t2 = new Tile("G","9");
	    Tile t3 = new Tile("B", "9");
	    Tile t4 = new Tile("R", "1");
	    Tile t5 = new Tile("G","1");
	    Tile t6 = new Tile("B", "1");
	    Tile t7 = new Tile("O", "5");
	    Tile t8 = new Tile("O", "6");
	    Tile t9 = new Tile("O", "7");
	    Tile t10 = new Tile("O", "8");
	    Tile t11 = new Tile("O", "9");
	   
	    h.addTile(t1);
	    h.addTile(t2);
	    h.addTile(t3);
	    h.addTile(t4);
	    h.addTile(t5);
	    h.addTile(t6);
	    h.addTile(t7);
	    h.addTile(t8);
	    h.addTile(t9);
	    h.addTile(t10);
	    h.addTile(t11);
	    
	    meld1.add(t1);
	    meld1.add(t2);
	    meld1.add(t3);
	    
	    meld2.add(t4);
	    meld2.add(t5);
	    meld2.add(t6);
	    
	    remainingTiles.add(t7);
	    remainingTiles.add(t8);
	    remainingTiles.add(t9);
	    remainingTiles.add(t10);
	    remainingTiles.add(t11);
	    
	    h.playHandMeld(table, true);
	    
	    assertEquals(5, h.getCount());
	    assertEquals(meld1, table.get(0));
	    assertEquals(meld2, table.get(1));
	    assertEquals(remainingTiles, h.getHand());
	    
	}
}
