package tableObserver;

import static org.junit.Assert.assertArrayEquals;

import java.util.ArrayList;
import java.util.List;

import core.Tile;
import junit.framework.TestCase;
import observer.Game;

public class TableTesting extends TestCase{
	public void testAddingValidMelds() {
		Game tb = new Game();
		ArrayList<Tile> meld1 = new ArrayList<Tile>();
		ArrayList<Tile> meld2 = new ArrayList<Tile>();
		ArrayList<Tile> meld3 = new ArrayList<Tile>();
		ArrayList<Tile> meld4 = new ArrayList<Tile>();
		
		meld1.add(new Tile("R", "6"));
		meld1.add(new Tile("O", "6"));
		meld1.add(new Tile("G", "6"));
		
		meld2.add(new Tile("B", "8"));
		meld2.add(new Tile("B", "9"));
		meld2.add(new Tile("B", "10"));
		
		meld3.add(new Tile("R", "12"));
		meld3.add(new Tile("O", "12"));
		meld3.add(new Tile("G", "12"));
		meld3.add(new Tile("B", "12"));
		
		meld4.add(new Tile("O", "1"));
		meld4.add(new Tile("O", "2"));
		meld4.add(new Tile("O", "3"));
		meld4.add(new Tile("O", "4"));
		
		assertEquals(true, tb.addMeldToTable(meld1));
		assertEquals(true, tb.addMeldToTable(meld2));
		assertEquals(true, tb.addMeldToTable(meld3));
		assertEquals(true, tb.addMeldToTable(meld4));
	}
	
	public void testAddingInvalidMelds() {
		Game tb = new Game();
		ArrayList<Tile> meld1 = new ArrayList<Tile>();
		ArrayList<Tile> meld2 = new ArrayList<Tile>();
		ArrayList<Tile> meld3 = new ArrayList<Tile>();
		ArrayList<Tile> meld4 = new ArrayList<Tile>();
		ArrayList<Tile> meld5 = new ArrayList<Tile>();
		
		meld1.add(new Tile("R", "6"));
		meld1.add(new Tile("O", "6"));
		
		meld2.add(new Tile("B", "8"));
		meld2.add(new Tile("B", "9"));
		meld2.add(new Tile("B", "11"));
		
		meld3.add(new Tile("R", "12"));
		meld3.add(new Tile("O", "12"));
		meld3.add(new Tile("G", "12"));
		meld3.add(new Tile("O", "12"));
		
		meld4.add(new Tile("O", "1"));
		meld4.add(new Tile("O", "2"));
		meld4.add(new Tile("O", "3"));
		meld4.add(new Tile("G", "4"));
		
		meld5.add(new Tile("G", "6"));
		meld5.add(new Tile("O", "7"));
		
		assertEquals(false, tb.addMeldToTable(meld1));
		assertEquals(false, tb.addMeldToTable(meld2));
		assertEquals(false, tb.addMeldToTable(meld3));
		assertEquals(false, tb.addMeldToTable(meld4));
		assertEquals(false, tb.addMeldToTable(meld5));
	}
	
	public void testGettingTable() {
		Game tb = new Game();
		ArrayList<Tile> meld1 = new ArrayList<Tile>();
		ArrayList<Tile> meld2 = new ArrayList<Tile>();
		
		meld1.add(new Tile("R", "6"));
		meld1.add(new Tile("O", "6"));
		meld1.add(new Tile("G", "6"));
		
		meld2.add(new Tile("B", "8"));
		meld2.add(new Tile("B", "9"));
		meld2.add(new Tile("B", "10"));
		
		List<ArrayList<Tile>> tableMelds = new ArrayList<ArrayList<Tile>>();
		List<ArrayList<Tile>> melds = new ArrayList<ArrayList<Tile>>();
		
		tb.addMeldToTable(meld1);
		tb.addMeldToTable(meld2);
		
		melds.add(meld1);
		melds.add(meld2);
		
		tableMelds = tb.getTable();
		
		for(int i = 0; i < tableMelds.size(); i++) {
			for(int j = 0; j < tableMelds.get(i).size(); j++) {
				assertArrayEquals(melds.get(i).get(j).getInfo(), tableMelds.get(i).get(j).getInfo());
			}
		}
	}
	
	public void testSetTable() {
		Game table = new Game();
		ArrayList<Tile> meld1 = new ArrayList<Tile>();
		ArrayList<Tile> meld2 = new ArrayList<Tile>();
		ArrayList<Tile> meld3 = new ArrayList<Tile>();
		ArrayList<Tile> meld4 = new ArrayList<Tile>();
		
		meld1.add(new Tile("R", "6"));
		meld1.add(new Tile("O", "6"));
		meld1.add(new Tile("G", "6"));
		
		meld2.add(new Tile("B", "8"));
		meld2.add(new Tile("B", "9"));
		meld2.add(new Tile("B", "10"));
		
		meld3.add(new Tile("R", "12"));
		meld3.add(new Tile("O", "12"));
		meld3.add(new Tile("G", "12"));
		meld3.add(new Tile("B", "12"));
		
		meld4.add(new Tile("O", "1"));
		meld4.add(new Tile("O", "2"));
		meld4.add(new Tile("O", "3"));
		meld4.add(new Tile("O", "4"));
		
		table.addMeldToTable(meld1);
		table.addMeldToTable(meld2);
		table.addMeldToTable(meld3);
		
		assertEquals(meld3, table.setMeldOnTable(2, meld4));
		
		ArrayList<ArrayList<Tile>> tableTiles = new ArrayList<ArrayList<Tile>>(table.getTable());
		assertEquals(meld4,tableTiles.get(2));
		
		assertNull(table.setMeldOnTable(5, meld3));
		
	}
}
