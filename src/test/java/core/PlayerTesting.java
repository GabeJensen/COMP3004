package core;

import junit.framework.TestCase;
import tableObserver.Table;

public class PlayerTesting extends TestCase{
	
	public void testPlayerName() {
		Table t = new Table();
		Player jim = new Player(t, "Jim", new Strat1());
		Player noName = new Player(t, new Strat2());
		
		assertEquals(jim.getName(), "Jim");
		assertEquals(noName.getName(), "No Name");
	}
	
}
