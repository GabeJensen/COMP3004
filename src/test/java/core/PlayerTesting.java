package core;

import junit.framework.TestCase;
import tableObserver.Table;

public class PlayerTesting extends TestCase{
	
	public void testPlayerName() {
		Table t = new Table();
		Player jim = new Player(t, "Jim");
		Player noName = new Player(t);
		
		assertEquals(jim.getName(), "Jim");
		assertEquals(noName.getName(), "No Name");
	}
	
}
