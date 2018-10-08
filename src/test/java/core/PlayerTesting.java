package core;

import junit.framework.TestCase;

public class PlayerTesting extends TestCase{
	
	public void testPlayerName() {
		Player jim = new Player("Jim");
		Player noName = new Player();
		
		assertEquals(jim.getName(), "Jim");
		assertEquals(noName.getName(), "No Name");
	}
	
}
