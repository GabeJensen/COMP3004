package core;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class MementoTesting extends TestCase{
	public void testMemento() {
		// Testing the copy capabilities of the memento's methods
		List<ArrayList<Tile>> table = new ArrayList<ArrayList<Tile>>();
		ArrayList<Tile> hand = new ArrayList<Tile>();
		
		Tile t1 = new Tile("R", "9");
		Tile t2 = new Tile("R", "10");
		Tile t3 = new Tile("R", "11");
		Tile t4 = new Tile("R", "12");
		Tile t5 = new Tile("R", "4");
		Tile t6 = new Tile("O", "5");
		Tile t7 = new Tile("B", "5");
		Tile t8 = new Tile("G", "5");
		Tile t9 = new Tile("R", "5");
		Tile t10 = new Tile("O", "13");
		Tile t11 = new Tile("R", "6");
		Tile t12 = new Tile("G", "13");
		Tile t13 = new Tile("B", "13");
		Tile t14 = new Tile("R", "13");
		
		Tile[] meld1 = {t5, t9, t11};
		Tile[] meld2 = {t1, t2, t3, t4, t14};
		Tile[] meld3 = {t7, t8, t6};
		Tile[] meld4 = {t13, t12, t10};
		Tile[][] melds = {meld1, meld2, meld3, meld4};
		
		for(Tile[] meld : melds) {
			ArrayList<Tile> temp = new ArrayList<Tile>();
			for(Tile tile : meld) {
				temp.add(tile);
			}
			table.add(temp);
		}
		
		hand.add(t2);
		hand.add(t4);
		hand.add(t6);
		hand.add(t8);
	
		Memento state = new Memento(table, hand);
				
		for (int x = 0; x < table.size(); x++) {
			// New "melds" were created
			assertFalse(table.get(x) == state.getTable().get(x));
			for (int y = 0; y < table.get(x).size(); y++) {
				// New tiles were created
				assertFalse(table.get(x).get(y) == state.getTable().get(x).get(y));
			}
		}
		
		for (int i = 0; i < hand.size(); i++) {
			assertFalse(hand.get(i) == state.getHand().get(i));
		}
	}
}
