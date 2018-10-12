package core;

import tableObserver.Table;

public class Game {
	public Game() {
		Table table = new Table();
		Player user = new Player(table, "User");
		Player p1 = new Player(table, "P1");
		Player p2 = new Player(table, "P2");
		Player p3 = new Player(table, "P3");
	}
	
	public void playGame() {
		
	}
	
	public static void main(String args[]) {
		Game game = new Game();
		
		game.playGame();
	}
}
