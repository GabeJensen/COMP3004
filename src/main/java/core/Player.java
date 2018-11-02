package core;

import java.util.ArrayList;
import java.util.List;

import observer.Observer;
import observer.Subject;
import observer.Game;

public class Player implements Observer {
	private Hand hand;
	private String name;
	private boolean initial30;
	private Subject game;
	private List<ArrayList<Tile>> tableTiles;
	private PlayerStrategy playerStrat;
	private int smallestHandSizeInGame;
			
	public Player(Subject g, PlayerStrategy s) {
		this.hand = new Hand();
		this.name = "No Name";
		this.initial30 = false;
		this.playerStrat = s;
		this.game = g;
		this.game.registerObserver(this);
		this.tableTiles = new ArrayList<ArrayList<Tile>>();
		this.smallestHandSizeInGame = 0;
	}
	
	public Player(Subject g, String name, PlayerStrategy s) {
		this.hand = new Hand();
		this.name = name;
		this.initial30 = false;
		this.playerStrat = s;
		this.game = g;
		this.game.registerObserver(this);
		this.tableTiles = new ArrayList<ArrayList<Tile>>();
		this.smallestHandSizeInGame = 0;
	}
	
	@Override
	public void update() {
		// Player will use this table in some way, either to play hand melds and/or table melds.
		Game g = (Game) game;
		this.tableTiles = g.getTable();
		this.smallestHandSizeInGame = g.getSmallestHandSize();
	}
	
	public String getName() {
		return name;
	}
	
	public int getHandCount() {
		return hand.getCount();
	}
	
	public ArrayList<Tile> getTiles(){
		return hand.getHand();
	}
	
	public void setTiles(ArrayList<Tile> newHand) {
		this.hand.setHand(newHand);
	}
	
	public int performStrategy() {
		int returnValue;
		
		returnValue = playerStrat.strat(hand, initial30, tableTiles, smallestHandSizeInGame);
		
		if(returnValue != 0 && !initial30) {
			initial30 = true;
		}
		
		ArrayList<ArrayList<Tile>> tempTableTiles = new ArrayList<ArrayList<Tile>>(tableTiles);
		ArrayList<ArrayList<Tile>> tempTable = new ArrayList<ArrayList<Tile>>(((Game)game).getTable());
		
		int tempTableTilesSize = tempTableTiles.size();
		int tempTableSize = tempTable.size();
		
		//Since the AI never reduces the number of melds on the table
		//The existing meld positions are looped through and set with the modified melds
		for(int i = 0; i < tempTableSize; i++) {
			((Game)game).setMeldOnTable(i,tempTableTiles.get(i));
		}
		
		//If tableTiles is larger then the remaining melds are added onto the table
		for(int i = tempTableSize; i < tempTableTilesSize; i++) {
			((Game)game).addMeldToTable(tempTableTiles.get(i));
		}
		

		// Need to have some distinction for when the player is able to play cards or not, so that in Game.java, it will draw deck based on this function's return.
		//Returning 0 means no melds played, 1 means cards were played
		return returnValue;
	}
	
	public void setStrategy(PlayerStrategy s) {
		this.playerStrat = s;
	}
	
	public boolean getInit30Flag() {
		return this.initial30;
	}
	
	public void playedInit30() {
		this.initial30 = true;
	}
	
	public void addTile(Tile tile) {
		hand.addTile(tile);
	}
	
	public Tile removeTile(Tile remove) {
		return hand.removeTile(remove);
	}
}
