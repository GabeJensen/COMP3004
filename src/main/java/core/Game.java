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
		//Create and initialize GUI
		/* Create buttons for:
		 * - Starting the game
		 * - Reseting table to start of turn
		 * - Ending your turn (checking melds)
		 * */
		
		//Create deck
		//Set tiles for users using deck
		/* allUsers = {user, p1, p2, p3}
		 * while true
		 * 	foreach(player in allUsers)
		 * 		tiles = player.strat()
		 * 		table.addMeldsToTable(tiles)
		 * 		if player tiles == 0
		 * 			end game
		 * 		table.notifyObservers()  <- I'm not sure if this is done here or in the player class
		 * 		GUI.update()
		 * 	*/
	}
	
	public static void main(String args[]) {
		Game game = new Game();
		
		game.playGame();
	}
}
