package core;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;
import observer.Game;

public class GameplayTesting extends TestCase{
	
	public void testStrat1a() {
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
	
	public void testStrat1b() {
		//Tests playing hand melds after the initial30
		Game table = new Game();
		Player p1 = new Player(table, "P1", new Strat1());

		Tile tile1 = new Tile("R", "1");
		Tile tile2 = new Tile("O", "2");
		Tile tile3 = new Tile("B", "3");
		Tile tile4 = new Tile("R", "10");
		Tile tile5 = new Tile("B", "10");
		Tile tile6 = new Tile("G", "10");
		Tile tile7 = new Tile("O", "10");
		Tile tile8 = new Tile("B", "5");
		Tile tile9 = new Tile("O", "7");
		Tile tile10 = new Tile("O", "5");
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
		
		p1.addTile(new Tile("O", "6"));
		
		stratResult = p1.performStrategy();
		
		assertEquals(1, stratResult);
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
		int stratResult;
		Game table = new Game();
		Player player = new Player(table, "P3", new Strat3());
		
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
		
		Tile[] playerHand = {t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14};
		Tile[] meld1 = {t5, t9, t11};
		Tile[] meld2 = {t1, t2, t3, t4, t14};
		Tile[] meld3 = {t7, t8, t6};
		Tile[] meld4 = {t13, t12, t10};
		ArrayList<ArrayList<Tile>> actualTable = new ArrayList<ArrayList<Tile>>();
		Tile[][] melds = {meld1, meld2, meld3, meld4};
		
		for(Tile[] meld : melds) {
			ArrayList<Tile> temp = new ArrayList<Tile>();
			for(Tile tile : meld) {
				temp.add(tile);
			}
			actualTable.add(temp);
		}
		
		for(Tile t : playerHand) {
			player.addTile(t);
		}
		
		stratResult = player.performStrategy();
		 
		assertEquals(1, stratResult);
		assertEquals(0, player.getHandCount());
		assertEquals(actualTable, table.getTable());
	}
	
	public void testStrat3b() {
		//Test play table tiles when having 3 more tiles than another player
		int stratResult;
		Game table = new Game();
		Player user = new Player(table, "User", new Strat0());
		Player p1 = new Player(table, "P1", new Strat1());
		Player p2 = new Player(table, "P2", new Strat2());
		Player p3 = new Player(table, "P3", new Strat3());
		table.setPlayers(user, p1, p2, p3);
		
		Tile t1 = new Tile("R", "9");
		Tile t2 = new Tile("R", "10");
		Tile t3 = new Tile("R", "11");
		Tile t4 = new Tile("R", "4");
		Tile t5 = new Tile("O", "5");
		Tile t6 = new Tile("B", "5");
		Tile t7 = new Tile("G", "5");
		
		Tile t8 = new Tile("O", "13");
		Tile t9 = new Tile("R", "6");
		Tile t10 = new Tile("G", "13");
		
		Tile m1 = new Tile("R", "5");
		Tile m2 = new Tile("R", "6");
		Tile m3 = new Tile("R", "7");

		Tile[] userMelds = {t8};
		Tile[] p1Melds = {t9};
		Tile[] p2Melds = {t10};
		Tile[] p3Melds = {t1, t2, t3, t4, t5, t6};
		ArrayList<Tile> meld1 = new ArrayList<Tile>();
		ArrayList<Tile> meld2 = new ArrayList<Tile>();
		meld1.add(m1);
		meld1.add(m2);
		meld1.add(m3);
		
		meld2.add(t6);
		meld2.add(t7);
		meld2.add(t5);
		
		for(Tile tile : userMelds) {
			user.addTile(tile);
		}
		
		for(Tile tile : p1Melds) {
			p1.addTile(tile);
		}
		
		for(Tile tile : p2Melds) {
			p2.addTile(tile);
		}
		
		for(Tile tile : p3Melds) {
			p3.addTile(tile);
		}
		
		stratResult = p3.performStrategy();
		
		assertEquals(1, stratResult);
		
		table.addMeldToTable(meld1);
		
		p3.addTile(t7);
		
		stratResult = p3.performStrategy();
		
		meld1.add(0, t4);
		
		assertEquals(1, stratResult);
		assertEquals(meld1, table.getTable().get(1));
		assertEquals(meld2, table.getTable().get(2));
		
	}
	
	public void testStrat3c() {
		//Test play all tiles when they dont have 3 more tiles than any other player
		int stratResult;
		Game table = new Game();
		Player user = new Player(table, "User", new Strat0());
		Player p1 = new Player(table, "P1", new Strat1());
		Player p2 = new Player(table, "P2", new Strat2());
		Player p3 = new Player(table, "P3", new Strat3());
		table.setPlayers(user, p1, p2, p3);
		
		Tile t1 = new Tile("R", "9");
		Tile t2 = new Tile("R", "10");
		Tile t3 = new Tile("R", "11");
		Tile t4 = new Tile("R", "4");
		Tile t5 = new Tile("O", "5");
		Tile t6 = new Tile("B", "5");
		Tile t7 = new Tile("G", "5");
		
		Tile t8 = new Tile("O", "13");
		Tile t9 = new Tile("R", "6");
		Tile t10 = new Tile("G", "13");
		
		Tile m1 = new Tile("R", "5");
		Tile m2 = new Tile("R", "6");
		Tile m3 = new Tile("R", "7");

		Tile[] userMelds = {t8, t9, t10};
		Tile[] p1Melds = {t8, t9, t10};
		Tile[] p2Melds = {t8, t9, t10};
		Tile[] p3Melds = {t1, t2, t3, t4, t5, t6};
		ArrayList<Tile> meld1 = new ArrayList<Tile>();
		meld1.add(m1);
		meld1.add(m2);
		meld1.add(m3);
		
		for(Tile tile : userMelds) {
			user.addTile(tile);
		}
		
		for(Tile tile : p1Melds) {
			p1.addTile(tile);
		}
		
		for(Tile tile : p2Melds) {
			p2.addTile(tile);
		}
		
		for(Tile tile : p3Melds) {
			p3.addTile(tile);
		}
		
		stratResult = p3.performStrategy();
		
		assertEquals(1, stratResult);
		
		p3.addTile(t7);
		
		table.addMeldToTable(meld1);
		
		stratResult = p3.performStrategy();
		
		meld1.add(0, t4);
		
		assertEquals(1, stratResult);
		assertEquals(meld1, table.getTable().get(1));
	}
	
	public void testStrat1c() {
		//Test playing multiple sets 
		Game table = new Game();
		Player p1 = new Player(table, "P1", new Strat1());

		Tile tile1 = new Tile("R", "3");
		Tile tile2 = new Tile("O", "3");
		Tile tile3 = new Tile("B", "3");
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
		Tile[] tableTiles = {tile1, tile3, tile2, tile4, tile5, tile6, tile7};
		Tile[] meld1 = {tile1, tile3, tile2};
		Tile[] meld2 = {tile4, tile5, tile6, tile7};
		
		for(int i = 0; i < 13; i++) {
			p1.addTile(tiles[i]);
		}
		
		int stratResult;
		//Returns 0 if draw card else it played something
		stratResult = p1.performStrategy();
		
		assertEquals(1, stratResult);
		
		List<ArrayList<Tile>> tableGetter = table.getTable();
		
		for(int i = 0; i < 3; i++) {
			assertArrayEquals(meld1[i].getInfo(), tableGetter.get(0).get(i).getInfo());
		}
		for(int i = 0; i < 4; i++) {
			assertArrayEquals(meld2[i].getInfo(), tableGetter.get(1).get(i).getInfo());
		}
		
	}
	
	public void testStrat1d() {
		//Test playing one meld on first turn then one meld on a subsequent turn
		Game table = new Game();
		Player p1 = new Player(table, "P1", new Strat1());

		Tile tile1 = new Tile("R", "3");
		Tile tile2 = new Tile("O", "3");
		Tile tile3 = new Tile("B", "3");
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
		
		Tile[] tiles = {tile1, tile2, tile4, tile5, tile6, tile7, tile8, tile9, tile10, tile11, tile12, tile13};
		Tile[] tableTiles = {tile1, tile3, tile2, tile4, tile5, tile6, tile7};
		Tile[] meld1 = {tile1, tile3, tile2};
		Tile[] meld2 = {tile4, tile5, tile6, tile7};
		
		for(int i = 0; i < 12; i++) {
			p1.addTile(tiles[i]);
		}
		
		int stratResult;
		
		stratResult = p1.performStrategy();
		
		assertEquals(1, stratResult);
		
		List<ArrayList<Tile>> tableGetter = table.getTable();
		
		for(int i = 0; i < 4; i++) {
			assertArrayEquals(meld2[i].getInfo(), tableGetter.get(0).get(i).getInfo());
		}
		
		stratResult = p1.performStrategy();
		
		assertEquals(0, stratResult);
		
		p1.addTile(tile3);
		
		stratResult = p1.performStrategy();
		
		assertEquals(1, stratResult);
		
		for(int i = 0; i < 3; i++) {
			assertArrayEquals(meld1[i].getInfo(), tableGetter.get(1).get(i).getInfo());
		}
	}
	
	public void testStrat1e() {
		//Test drawing on first turn, subsequent turn and playing several melds in a subsequent meld
		Game table = new Game();
		Player p1 = new Player(table, "P1", new Strat1());

		Tile tile1 = new Tile("R", "1");
		Tile tile2 = new Tile("O", "1");
		Tile tile3 = new Tile("B", "1");
		Tile tile4 = new Tile("R", "8");
		Tile tile5 = new Tile("B", "8");
		Tile tile6 = new Tile("G", "8");
		Tile tile7 = new Tile("O", "8");
		Tile tile8 = new Tile("B", "5");
		Tile tile9 = new Tile("O", "7");
		Tile tile10 = new Tile("R", "5");
		Tile tile11 = new Tile("B", "5");
		Tile tile12 = new Tile("B", "6");
		Tile tile13 = new Tile("O", "13");
		
		Tile[] tiles = {tile1, tile2, tile3, tile4, tile5, tile8, tile9, tile10, tile11, tile12, tile13};
		Tile[] tableTiles = {tile1, tile3, tile2, tile4, tile5, tile6, tile7};
		Tile[] meld1 = {tile1, tile3, tile2};
		Tile[] meld2 = {tile4, tile5, tile6, tile7};
		
		for(int i = 0; i < 11; i++) {
			p1.addTile(tiles[i]);
		}
		
		int stratResult;
		
		stratResult = p1.performStrategy();
		
		assertEquals(0, stratResult);
		
		p1.addTile(tile6);
		
		stratResult = p1.performStrategy();
		
		assertEquals(0, stratResult);
		
		p1.addTile(tile7);
		
		stratResult = p1.performStrategy();
		
		assertEquals(1, stratResult);
		
		List<ArrayList<Tile>> tableGetter = table.getTable();
		
		for(int i = 0; i < 3; i++) {
			assertArrayEquals(meld1[i].getInfo(), tableGetter.get(0).get(i).getInfo());
		}

		for(int i = 0; i < 4; i++) {
			assertArrayEquals(meld2[i].getInfo(), tableGetter.get(1).get(i).getInfo());
		}
		
	}
	
	public void testStrat3d() {
		//Test p3 playing 30+ points on subsequent turn, p3 winning using table tiles, and p3 playing all tiles because another player has 3 less tiles  
		int stratResult;
		Game table = new Game();
		Player user = new Player(table, "User", new Strat0());
		Player p1 = new Player(table, "P1", new Strat1());
		Player p2 = new Player(table, "P2", new Strat2());
		Player p3 = new Player(table, "P3", new Strat3());
		table.setPlayers(user, p1, p2, p3);

		Tile t1 = new Tile("R", "9");
		Tile t2 = new Tile("R", "10");
		Tile t3 = new Tile("R", "11");
		Tile t4 = new Tile("R", "4");
		Tile t5 = new Tile("O", "5");
		Tile t6 = new Tile("B", "5");
		Tile t7 = new Tile("G", "5");
		
		Tile t8 = new Tile("O", "13");
		Tile t9 = new Tile("R", "6");
		Tile t10 = new Tile("G", "13");
		
		Tile m1 = new Tile("R", "5");
		Tile m2 = new Tile("R", "6");
		Tile m3 = new Tile("R", "7");

		Tile[] userMelds = {t8, t9, t10};
		Tile[] p1Melds = {t8, t9, t10};
		Tile[] p2Melds = {t8, t9, t10};
		Tile[] p3Melds = {t1, t2, t3, t4, t5, t6};
		ArrayList<Tile> meld1 = new ArrayList<Tile>();
		meld1.add(m1);
		meld1.add(m2);
		meld1.add(m3);
		
		for(Tile tile : userMelds) {
			user.addTile(tile);
		}
		
		for(Tile tile : p1Melds) {
			p1.addTile(tile);
		}
		
		for(Tile tile : p2Melds) {
			p2.addTile(tile);
		}
		
		for(Tile tile : p3Melds) {
			p3.addTile(tile);
		}
		
		stratResult = p3.performStrategy();
		
		assertEquals(1, stratResult);
		
		p3.addTile(t7);
		
		table.addMeldToTable(meld1);
		
		stratResult = p3.performStrategy();
		
		meld1.add(0, t4);
		
		assertEquals(1, stratResult);
		assertEquals(meld1, table.getTable().get(1));
	}
	
	public void testStrat2e() {
		//Tests drawing after another player plays initial 30
		int stratResult;
		Game table = new Game();
		Player player = new Player(table, "P2", new Strat2());
		
		Tile t1 = new Tile("R", "1");
		Tile t2 = new Tile("R", "3");
		Tile t3 = new Tile("R", "5");
		Tile t4 = new Tile("R", "7");
		Tile t5 = new Tile("B", "4");
		Tile t6 = new Tile("O", "11");
		Tile t7 = new Tile("B", "7");
		Tile t8 = new Tile("G", "5");
		Tile t9 = new Tile("R", "5");
		Tile t10 = new Tile("O", "13");
		Tile t11 = new Tile("O", "6");
		Tile t12 = new Tile("G", "13");
		Tile t13 = new Tile("R", "12");
		Tile t14 = new Tile("B", "9");
		
		Tile[] playerHand = {t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14};
		
		for(Tile t : playerHand) {
			player.addTile(t);
		}
		
		ArrayList<Tile> meld1 = new ArrayList<Tile>();
		
		Tile m1 = new Tile("O", "8");
		Tile m2 = new Tile("R", "8");
		Tile m3 = new Tile("G", "8");
		Tile m4 = new Tile("B", "8");
		//P3 would be drawing after this
		stratResult = player.performStrategy();
		
		assertEquals(0, stratResult);
			
	}
	
	public void testStrat1f() {
		//Tests winning on first hand
		int stratResult;
		Game table = new Game();
		Player p1 = new Player(table, "P1", new Strat1());
		
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
		Tile tile14 = new Tile("O", "5");
		
		Tile[] handTiles = {tile1, tile2, tile3, tile4, tile5, tile6, tile7, tile8, tile9, tile10, tile11, tile12, tile13, tile14};
		
		for (Tile t: handTiles) {
			p1.addTile(t);
		}
		
		stratResult = p1.performStrategy();
		
		assertEquals(0, p1.getHandCount());
		assertEquals(1, stratResult);
	}
	
	public void testStrat1g() {
		//Test playing exactly 30 on inital play using multiple melds
		Game table = new Game();
		Player p1 = new Player(table, "P1", new Strat1());

		Tile tile1 = new Tile("R", "1");
		Tile tile2 = new Tile("R", "2");
		Tile tile3 = new Tile("R", "3");
		Tile tile4 = new Tile("R", "8");
		Tile tile5 = new Tile("B", "8");
		Tile tile6 = new Tile("G", "8");
		Tile tile7 = new Tile("O", "10");
		Tile tile8 = new Tile("B", "5");
		Tile tile9 = new Tile("O", "7");
		Tile tile10 = new Tile("R", "5");
		Tile tile11 = new Tile("B", "5");
		Tile tile12 = new Tile("B", "13");
		Tile tile13 = new Tile("O", "2");
		
		Tile[] tiles = {tile1, tile2, tile3, tile4, tile5, tile6, tile7, tile8, tile9, tile10, tile11, tile12, tile13};
		Tile[] tableTiles = {tile1, tile2, tile3, tile4, tile5, tile6};
		Tile[] meld1 = {tile1, tile2, tile3};
		Tile[] meld2 = {tile4, tile5, tile6};
		
		for(int i = 0; i < 13; i++) {
			p1.addTile(tiles[i]);
		}
		
		int stratResult;

		stratResult = p1.performStrategy();
		
		assertEquals(1, stratResult);
		
		List<ArrayList<Tile>> tableGetter = table.getTable();
		
		for(int i = 0; i < 3; i++) {
			assertArrayEquals(meld1[i].getInfo(), tableGetter.get(0).get(i).getInfo());
		}
		for(int i = 0; i < 3; i++) {
			assertArrayEquals(meld2[i].getInfo(), tableGetter.get(1).get(i).getInfo());
		}
	
	}
	
	public void testStrat1h() {
		//Test playing several tiles to add to several melds on the table
		int stratResult;
		Game table = new Game();
		Player p1 = new Player(table, "P1", new Strat1());

		Tile t1 = new Tile("R", "9");
		Tile t2 = new Tile("R", "10");
		Tile t3 = new Tile("R", "11");
		Tile t4 = new Tile("R", "4");
		Tile t5 = new Tile("O", "5");
		Tile t6 = new Tile("B", "6");
		Tile t7 = new Tile("G", "7");
		Tile t8 = new Tile("R", "9");
		Tile t9 = new Tile("R", "13");
		Tile t10 = new Tile("B", "10");
		Tile t11 = new Tile("R", "2");
		Tile t12 = new Tile("O", "1");
		Tile t13 = new Tile("B", "2");
		Tile t14 = new Tile("G", "3");
		
		Tile m1 = new Tile("R", "3");
		Tile m2 = new Tile("R", "4");
		Tile m3 = new Tile("R", "5");
		
		Tile m4 = new Tile("R", "3");
		Tile m5 = new Tile("B", "3");
		Tile m6 = new Tile("O", "3");

		Tile[] p1Tiles = {t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14};
		
		ArrayList<Tile> meld1 = new ArrayList<Tile>();
		meld1.add(m1);
		meld1.add(m2);
		meld1.add(m3);
		
		ArrayList<Tile> meld2 = new ArrayList<Tile>();
		meld2.add(m4);
		meld2.add(m5);
		meld2.add(m6);
		
		for(Tile tile : p1Tiles) {
			p1.addTile(tile);
		}
		
		stratResult = p1.performStrategy();
		
		assertEquals(1, stratResult);
		
		table.addMeldToTable(meld1);
		table.addMeldToTable(meld2);
		
		stratResult = p1.performStrategy();
		
		meld1.add(0, t11);
		meld2.add(0, t14);
		
		assertEquals(1, stratResult);
		assertEquals(meld1, table.getTable().get(1));
		assertEquals(meld2, table.getTable().get(2));
	}
	
	public void testStrat3e() {
		//Test play table tiles when having 3 more tiles than another player
		int stratResult;
		Game table = new Game();
		Player user = new Player(table, "User", new Strat0());
		Player p1 = new Player(table, "P1", new Strat1());
		Player p2 = new Player(table, "P2", new Strat2());
		Player p3 = new Player(table, "P3", new Strat3());
		table.setPlayers(user, p1, p2, p3);
		
		Tile t1 = new Tile("R", "9");
		Tile t2 = new Tile("R", "13");
		Tile t3 = new Tile("R", "11");
		Tile t4 = new Tile("R", "4");
		Tile t5 = new Tile("O", "5");
		Tile t6 = new Tile("B", "5");
		Tile t7 = new Tile("G", "6");
		
		Tile t8 = new Tile("O", "13");
		Tile t9 = new Tile("R", "6");
		Tile t10 = new Tile("G", "13");

		Tile[] userMelds = {t8};
		Tile[] p1Melds = {t9};
		Tile[] p2Melds = {t10};
		Tile[] p3Melds = {t1, t2, t3, t4, t5, t6, t7};
		
		for(Tile tile : userMelds) {
			user.addTile(tile);
		}
		
		for(Tile tile : p1Melds) {
			p1.addTile(tile);
		}
		
		for(Tile tile : p2Melds) {
			p2.addTile(tile);
		}
		
		for(Tile tile : p3Melds) {
			p3.addTile(tile);
		}
		
		stratResult = p3.performStrategy();
		
		assertEquals(0, stratResult);
		
	}
	
	public void testStrat4a() {
		int stratResult;
		Game game = new Game();
		Player p4 = new Player(game, "P4", new Strat4());
		
		// Melds on the table
		Tile t1 = new Tile("R", "4");
		Tile t2 = new Tile("R", "5");
		Tile t3 = new Tile("R", "6");
		Tile t4 = new Tile("B", "5");
		Tile t5 = new Tile("G", "5");
		Tile t6 = new Tile("O", "5");
		Tile t7 = new Tile("R", "1");
		Tile t8 = new Tile("B", "1");
		Tile t9 = new Tile("G", "1");
		Tile t10 = new Tile("O", "1");
		Tile t11 = new Tile("B", "3");
		Tile t12 = new Tile("B", "4");
		Tile t13 = new Tile("B", "5");
		Tile t14 = new Tile("G", "2");
		Tile t15 = new Tile("G", "3");
		Tile t16 = new Tile("G", "4");
		
		// Tiles in P4 hand
		Tile userT1 = new Tile("R", "2"); // will get discarded, both R1 and R4 are seen on the table, making this meld unlikely to be completed
		Tile userT2 = new Tile("R", "3"); // will get discarded, both R1 and R4 are seen on the table, making this meld unlikely to be completed
		Tile userT3 = new Tile("B", "6");
		Tile userT4 = new Tile("O", "6");
		Tile userT5 = new Tile("G", "5"); // will get discarded, 5 or more are on the table
		Tile userT6 = new Tile("O", "1");
		Tile userT7 = new Tile("R", "9");
		Tile userT8 = new Tile("R", "10");
		Tile userT9 = new Tile("R", "5"); // will get discarded, 5 or more are on the table
		
		// table melds
		ArrayList<Tile> meld1 = new ArrayList<Tile>(Arrays.asList(t1, t2, t3)); // R4, R5, R6
		ArrayList<Tile> meld2 = new ArrayList<Tile>(Arrays.asList(t4, t5, t6)); // B5, G5, O5
		ArrayList<Tile> meld3 = new ArrayList<Tile>(Arrays.asList(t7, t8, t9, t10)); // R1, B1, G1, O1
		ArrayList<Tile> meld4 = new ArrayList<Tile>(Arrays.asList(t11, t12, t13)); // B3, B4, B5
		ArrayList<Tile> meld5 = new ArrayList<Tile>(Arrays.asList(t14, t15, t16)); // G2, G3, G4
		
		// new table meld after start 4 is performed
		ArrayList<Tile> newMeld1 = new ArrayList<Tile>(Arrays.asList(userT2, t1, t2, t3));
		ArrayList<Tile> newMeld2 = new ArrayList<Tile>(Arrays.asList(userT9, t4, t5, t6));
		ArrayList<Tile> newMeld5 = new ArrayList<Tile>(Arrays.asList(t14, t15, t16, userT5));
		
		ArrayList<Tile> p4RemainingTilesInHand = new ArrayList<Tile>(Arrays.asList(userT1, userT7, userT8, userT3, userT6, userT4)); // might need to add userT1 and userT2 in here if it does not get played as table meld R1,R2,R3
		
		p4.addTile(userT1);
		p4.addTile(userT2);
		p4.addTile(userT3);
		p4.addTile(userT4);
		p4.addTile(userT5);
		p4.addTile(userT6);
		p4.addTile(userT7);
		p4.addTile(userT8);
		p4.addTile(userT9);
		
		game.addMeldToTable(meld1);
		game.addMeldToTable(meld2);
		game.addMeldToTable(meld3);
		game.addMeldToTable(meld4);
		game.addMeldToTable(meld5);
		
		// assume that P4 has already played their initial 30 points
		p4.playedInit30();
		stratResult = p4.performStrategy();
		
		//check if the strat played tiles (in particular, R2, R3, G5 and R5)
		assertEquals(1, stratResult);
		//check if the start discarded the proper tiles
		assertEquals(p4RemainingTilesInHand, p4.getTiles());
		//check if table has the new melds
		assertEquals(true, game.getTable().contains(newMeld1));
		assertEquals(true, game.getTable().contains(newMeld2));
		assertEquals(true, game.getTable().contains(newMeld5));
	}
}
