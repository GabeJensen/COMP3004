package tableObserver;

import static org.junit.Assert.assertArrayEquals;

import java.util.ArrayList;

import core.Tile;
import junit.framework.TestCase;
import tableObserver.Table;

public class TableTesting extends TestCase{
	public void testAddingValidMelds() {
		Table tb = new Table();
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
		Table tb = new Table();
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
		Table tb = new Table();
		ArrayList<Tile> meld1 = new ArrayList<Tile>();
		ArrayList<Tile> meld2 = new ArrayList<Tile>();
		
		meld1.add(new Tile("R", "6"));
		meld1.add(new Tile("O", "6"));
		meld1.add(new Tile("G", "6"));
		
		meld2.add(new Tile("B", "8"));
		meld2.add(new Tile("B", "9"));
		meld2.add(new Tile("B", "10"));
		
		ArrayList<ArrayList<Tile>> tableMelds = new ArrayList<ArrayList<Tile>>();
		
		tb.addMeldToTable(meld1);
		tb.addMeldToTable(meld2);
		
		tableMelds = tb.getTable();
		
		for(int i = 0; i < tableMelds.size(); i++) {
			for(int j = 0; j < tableMelds.get(i).size(); j++) {
				assertArrayEquals(meld1.get(j).getInfo(), tableMelds.get(i).get(j).getInfo());
			}
		}
	}
}
