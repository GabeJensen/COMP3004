package core;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import observer.Game;

public class GameplayTesting extends TestCase{
	
	public void testStrat1() {
		Game table = new Game();
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
	
	public void testStrat2a() {
		//This test does not test for "if it can play all its tiles, it does" case
		int stratResult;
		Game table = new Game();
		Player p2 = new Player(table, "P2", new Strat2());

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
		
		Tile[] handTiles = {tile1, tile2, tile3, tile4, tile5, tile6, tile7, tile8, tile9, tile10, tile11, tile12, tile13};
		Tile[] meldOnTable = {new Tile("R", "11"), new Tile("R", "12"), new Tile("R", "13")};
		Tile[] meldOnTable2 = {new Tile("B", "6"), new Tile("B", "7"), new Tile("B", "8")};
		Tile[] meldOnTable3 = {new Tile("R", "8"), new Tile("B", "8"), new Tile("G", "8")};
		Tile[] initialMeldTiles = {tile4, tile5, tile6, tile7};
		Tile[] p2FirstTableMelding = {tile8, new Tile("B", "6"), new Tile("B", "7"), new Tile("B", "8")};
		Tile[] p2SecondTableMelding = {tile13, new Tile("R", "8"), new Tile("B", "8"), new Tile("G", "8")};
		
		ArrayList<Tile> firstMeldOnTable = new ArrayList<Tile>();
		ArrayList<Tile> secondMeldOnTable = new ArrayList<Tile>();
		ArrayList<Tile> thirdMeldOnTable = new ArrayList<Tile>();
		ArrayList<Tile> initialMeldToPlay = new ArrayList<Tile>();
		ArrayList<Tile> secondMeldToPlay = new ArrayList<Tile>();
		ArrayList<Tile> thirdMeldToPlay = new ArrayList<Tile>();
		
		for (Tile t: handTiles) {
			p2.addTile(t);
		}
		for (Tile t: meldOnTable) {
			firstMeldOnTable.add(t);
		}
		for (Tile t: meldOnTable2) {
			secondMeldOnTable.add(t);
		}
		for (Tile t: meldOnTable3) {
			thirdMeldOnTable.add(t);
		}
		for (Tile t: initialMeldTiles) {
			initialMeldToPlay.add(t);
		}
		for (Tile t: p2FirstTableMelding) {
			secondMeldToPlay.add(t);
		}
		for (Tile t: p2SecondTableMelding) {
			thirdMeldToPlay.add(t);
		}
		
		//p2's turn, table is empty, hand has meld > 30, draw card
		stratResult = p2.performStrategy();
		assertEquals(0, stratResult);
		
		//simulated tile drawing
		p2.addTile(new Tile("B", "1"));
		
		//another player has played their initial meld > 30
		table.addMeldToTable(firstMeldOnTable);
		table.notifyObservers();
		//p2's turn, table is not empty, hand has meld > 30, play that meld
		stratResult = p2.performStrategy();
		assertEquals(1, stratResult);
		
		
		//check if performStrategy() play the correct initial meld
		List<ArrayList<Tile>> meldsOnTheTable = table.getTable();
		assertEquals(initialMeldToPlay, meldsOnTheTable.get(1)); //might need to loop through the arrays to assertEquals each tile
		
		//other players played 2 additional melds
		table.addMeldToTable(secondMeldOnTable);
		table.addMeldToTable(thirdMeldOnTable);
		table.notifyObservers();
		//p2's turn
		stratResult = p2.performStrategy();
		assertEquals(1, stratResult);
		
		//check if performStrategy() played the correct tiles that required using table melds
		meldsOnTheTable = table.getTable();
		assertEquals(secondMeldToPlay, meldsOnTheTable.get(2)); //might need to loop through the arrays to assertEquals each tile
		assertEquals(thirdMeldToPlay, meldsOnTheTable.get(3)); //might need to loop through the arrays to assertEquals each tile
		
		//p2's turn, nobody else played any melds or added tiles to melds on the table
		//cannot play entire hand or use tiles in hand with table melds, draw card
		stratResult = p2.performStrategy();
		assertEquals(0, stratResult);
	}
	
	public void testStrat2b() {
		//This test is specifically for "if it can play all its tiles, it does" case (**not using any table melds)
		int stratResult;
		Game table = new Game();
		Player p2 = new Player(table, "P2", new Strat2());
		
		Tile tile1 = new Tile("R", "1");
		Tile tile2 = new Tile("R", "2");
		Tile tile3 = new Tile("R", "3");
		Tile tile4 = new Tile("B", "8");
		Tile tile5 = new Tile("B", "9");
		Tile tile6 = new Tile("B", "10");
		Tile tile7 = new Tile("B", "11");
		Tile tile8 = new Tile("G", "5");
		Tile tile9 = new Tile("G", "6");
		Tile tile10 = new Tile("G", "7");
		Tile tile11 = new Tile("O", "2");
		Tile tile12 = new Tile("O", "3");
		Tile tile13 = new Tile("O", "4");
		
		Tile[] handTiles = {tile1, tile2, tile3, tile4, tile5, tile6, tile7, tile8, tile9, tile10, tile11, tile12};
		Tile[] initialMeldTiles = {tile4, tile5, tile6, tile7};
		Tile[] meld1 = {tile1, tile2, tile3};
		Tile[] meld2 = {tile8, tile9, tile10};
		Tile[] meld3 = {tile11, tile12, tile13};
		Tile[] tableMeld = {new Tile("G", "11"), new Tile("G", "12"), new Tile("G", "13")};
		
		ArrayList<Tile> initialMeldToPlay = new ArrayList<Tile>();
		ArrayList<Tile> meldToPlay1 = new ArrayList<Tile>();
		ArrayList<Tile> meldToPlay2 = new ArrayList<Tile>();
		ArrayList<Tile> meldToPlay3 = new ArrayList<Tile>();
		ArrayList<Tile> meldOnTable1 = new ArrayList<Tile>();
		
		for (Tile t: handTiles) {
			p2.addTile(t);
		}
		for (Tile t: initialMeldTiles) {
			initialMeldToPlay.add(t);
		}
		for (Tile t: meld1) {
			meldToPlay1.add(t);
		}
		for (Tile t: meld2) {
			meldToPlay2.add(t);
		}
		for (Tile t: meld3) {
			meldToPlay3.add(t);
		}
		for (Tile t: tableMeld) {
			meldOnTable1.add(t);
		}
		
		//p2's turn, table is empty, hand has meld > 30, draw card
		stratResult = p2.performStrategy();
		assertEquals(0, stratResult);
		
		//simulated tile drawing
		p2.addTile(tile13);
		
		//another player has played their initial meld > 30
		table.addMeldToTable(meldOnTable1);
		table.notifyObservers();
		//p2's turn, table is not empty, hand has meld > 30, play that meld
		stratResult = p2.performStrategy();
		assertEquals(1, stratResult);
		
		//check if performStrategy() play the correct initial meld
		List<ArrayList<Tile>> meldsOnTheTable = table.getTable();
		assertEquals(initialMeldToPlay, meldsOnTheTable.get(4)); //might need to loop through the arrays to assertEquals each tile
		
		//nobody else plays any melds or adds tiles to table melds
		//p2's turn
		stratResult = p2.performStrategy();
		assertEquals(1, stratResult);
		
		//check if performStrategy() play all of the melds in hand b/c it can win
		meldsOnTheTable = table.getTable();
		assertEquals(meldToPlay1, meldsOnTheTable.get(1)); //might need to loop through the arrays to assertEquals each tile
		assertEquals(meldToPlay3, meldsOnTheTable.get(2)); //might need to loop through the arrays to assertEquals each tile
	}
	
	public void testStrat2c() {
		//This test is specifically for "if it can play all its tiles, it does" case (**using any table melds)
		int stratResult;
		Game table = new Game();
		Player player = new Player(table, "P2", new Strat2());
		
		Tile t1 = new Tile("R", "1");
		Tile t2 = new Tile("R", "2");
		Tile t3 = new Tile("R", "3");
		Tile t4 = new Tile("R", "4");
		Tile t5 = new Tile("O", "9");
		Tile t6 = new Tile("O", "5");
		Tile t7 = new Tile("G", "10");
		Tile t8 = new Tile("O", "10");
		Tile t9 = new Tile("B", "10");
		
		Tile[] playerHand = {t1, t2, t3, t4, t5, t6, t7, t8, t9};
		
		for(Tile t : playerHand) {
			player.addTile(t);
		}
		
		ArrayList<Tile> meld1 = new ArrayList<Tile>();
		
		Tile m1 = new Tile("O", "6");
		Tile m2 = new Tile("O", "7");
		Tile m3 = new Tile("O", "8");
		
		Tile[] firstMeld = {m1, m2, m3};
		
		for(Tile tile : firstMeld) {
			meld1.add(tile);			
		}
		
		table.addMeldToTable(meld1);
		
		stratResult = player.performStrategy();
		
		assertEquals(1, stratResult);
		
		assertEquals(6, player.getHandCount());
		
		stratResult = player.performStrategy();
		
		assertEquals(1, stratResult);
		
		assertEquals(0, player.getHandCount());
	}
	
	public void testStrat2d() {
		//Tests playing entire hand at first possible chance
		int stratResult;
		Game table = new Game();
		Player player = new Player(table, "P2", new Strat2());
		
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
		
		Tile[] playerHand = {t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13};
		
		for(Tile t : playerHand) {
			player.addTile(t);
		}
		
		stratResult = player.performStrategy();
		
		assertEquals(0, stratResult);
		
		player.addTile(new Tile("R", "13"));
		
		ArrayList<Tile> meld1 = new ArrayList<Tile>();
		
		Tile m1 = new Tile("O", "8");
		Tile m2 = new Tile("R", "8");
		Tile m3 = new Tile("G", "8");
		Tile m4 = new Tile("B", "8");
		
		Tile[] firstMeld = {m1, m2, m3, m4};
		
		for(Tile tile : firstMeld) {
			meld1.add(tile);			
		}
		
		table.addMeldToTable(meld1);
		
		stratResult = player.performStrategy();
		
		assertEquals(1, stratResult);
		assertEquals(0, player.getHandCount());
			
	}
	
	public void testStrat3a() {
		//Test play all tiles
	}
	
	public void testStrat3b() {
		//Test play table tiles when having 3 more tiles than another player
	}
	
	public void testStrat3c() {
		//Test play all tiles when they dont have 3 more tiles than any other player
	}
}
