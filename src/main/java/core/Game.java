package core;

import tableObserver.Table;

public class Game {
	public Table table;
	public Player user;
	public Player p1;
	public Player p2;
	public Player p3;
	private Deck deck;
	
	public Game() {
		this.table = new Table();
		this.user = new Player(table, "User", new Strat0());
		this.p1 = new Player(table, "P1", new Strat1());
		this.p2 = new Player(table, "P2", new Strat2());
		this.p3 = new Player(table, "P3", new Strat3());
	}
	
	public void playGame() {
		//Create and initialize GUI
		/* Create buttons for:
		 * - Starting the game
		 * - Reseting table to start of turn
		 * - Ending your turn (checking melds)
		 * */
		
		//Create deck
		deck = new Deck();
		deck.shuffleDeck();
		
		//Set tiles for users using deck
		Player[] allPlayers = new Player[] {user, p1, p2, p3};
		
		for (Player player : allPlayers) {
			for (int c = 0; c < 14; c++) {
				player.addTile(deck.dealTile());
			}
		}
		
		/* 
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
}
