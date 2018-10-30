package core;

import static org.junit.Assert.assertArrayEquals;

import java.util.ArrayList;

import junit.framework.TestCase;
import observer.Game;

public class PlayerTesting extends TestCase{
	
	public void testPlayerName() {
		Game t = new Game();
		Player jim = new Player(t, "Jim", new Strat1());
		Player noName = new Player(t, new Strat2());
		
		assertEquals(jim.getName(), "Jim");
		assertEquals(noName.getName(), "No Name");
	}
	
	public void testAddTiles() {
		Game t = new Game();
		Player player = new Player(t, new Strat0());
		Tile tile1 = new Tile("R", "5");
		Tile tile2 = new Tile("B", "5");
		Tile tile3 = new Tile("B", "5");
		
		ArrayList<Tile> tiles = new ArrayList<Tile>();
		
		tiles.add(tile1);
		tiles.add(tile2);
		tiles.add(tile3);
		
		player.addTile(tile1);
		player.addTile(tile2);
		player.addTile(tile3);
		
		ArrayList<Tile> playerTiles = player.getTiles();
		
		for(int i = 0; i < tiles.size(); i++) {
			assertArrayEquals(tiles.get(i).getInfo(), playerTiles.get(i).getInfo());
		}
	}
	
}
