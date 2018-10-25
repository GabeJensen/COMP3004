package core;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import tableObserver.Table;

public class GameplayTesting extends TestCase{
	
	public void testStrat1() {
		Table table = new Table();
		Player p1 = new Player(table, "P1", new Strat1());

		Tile tile1 = new Tile("R", "1");
		Tile tile2 = new Tile("R", "2");
		Tile tile3 = new Tile("R", "3");
		Tile tile4 = new Tile("R", "10");
		Tile tile5 = new Tile("B", "10");
		Tile tile6 = new Tile("G", "10");
		Tile tile7 = new Tile("O", "10");
		Tile tile8 = new Tile("B", "5");
		Tile tile9 = new Tile("O", "7");
		Tile tile10 = new Tile("R", "5");
		Tile tile11 = new Tile("B", "5");
		Tile tile12 = new Tile("B", "6");
		Tile tile13 = new Tile("O", "8");
		
		Tile[] tiles = {tile1, tile2, tile3, tile4, tile5, tile6, tile7, tile8, tile9, tile10, tile11, tile12, tile13};
		Tile[] tableTiles = {tile1, tile2, tile3, tile4, tile5, tile6, tile7};
		Tile[] meld1 = {tile1, tile2, tile3};
		Tile[] meld2 = {tile4, tile5, tile6, tile7};
		
		for(int i = 0; i < 13; i++) {
			p1.addTile(tiles[i]);
		}
		
		int stratResult;
		//Returns 0 if draw card else it played something
		stratResult = p1.performStrategy();
		
		assertNotEquals(0, stratResult);
		
		List<ArrayList<Tile>> tableGetter = table.getTable();
		
		for(int i = 0; i < 3; i++) {
			assertArrayEquals(meld1[i].getInfo(), tableGetter.get(0).get(i).getInfo());
		}
		for(int i = 0; i < 4; i++) {
			assertArrayEquals(meld2[i].getInfo(), tableGetter.get(1).get(i).getInfo());
		}
		
		
		table.notifyObservers();
		stratResult = p1.performStrategy();
		table.notifyObservers();
		assertEquals(0, stratResult);
		//Simulated drawing a card
		Tile newTile = new Tile("R", "4");
		p1.addTile(newTile);
		
		stratResult = p1.performStrategy();
		//Should add R4 and R5 to existing meld
		assertNotEquals(0, stratResult);
		
		tableGetter = table.getTable();
		Tile[] newMeld1 = {tile1, tile2, tile3, newTile, tile10};
		
		for(int i = 0; i < 5; i++) {
			assertArrayEquals(newMeld1[i].getInfo(), tableGetter.get(0).get(i).getInfo());
		}
	}
}
