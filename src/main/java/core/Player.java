package core;

import java.util.ArrayList;
import java.util.List;

import tableObserver.Observer;
import tableObserver.Subject;
import tableObserver.Table;

public class Player implements Observer {
	private Hand hand;
	private String name;
	private boolean initial30;
	private Subject table;
	private List<ArrayList<Tile>> tableTiles;
	private PlayerStrategy playerStrat;
			
	public Player(Subject t, PlayerStrategy s) {
		this.hand = new Hand();
		this.name = "No Name";
		this.initial30 = false;
		this.playerStrat = s;
		this.table = t;
		this.table.registerObserver(this);
		this.tableTiles = new ArrayList<ArrayList<Tile>>();
	}
	
	public Player(Subject t, String name, PlayerStrategy s) {
		this.hand = new Hand();
		this.name = name;
		this.initial30 = false;
		this.playerStrat = s;
		this.table = t;
		this.table.registerObserver(this);
		this.tableTiles = new ArrayList<ArrayList<Tile>>();
	}
	
	public void update(List<ArrayList<Tile>> tableMelds) {
		// Player will use this table in some way, either to play hand melds and/or table melds.
		this.tableTiles = tableMelds;
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
	
	public List<ArrayList<Tile>> getHandMelds(){
		return hand.getHandMelds();
	}
	public ArrayList<Tile> getTilesForTableMelds() {
		return hand.getTilesForTableMelds();
	}
	
	public Tile removeTile(int index) {
		return hand.removeTile(index);
	}
	
	public Tile removeTile(Tile remove) {
		return hand.removeTile(remove);
	}
	
	public int performStrategy() {
		int returnValue;
		
		returnValue = playerStrat.strat(hand, initial30, tableTiles);
		
		if(returnValue != 0 && !initial30) {
			initial30 = true;
		}
		
		ArrayList<ArrayList<Tile>> tempTableTiles = new ArrayList<ArrayList<Tile>>(tableTiles);
		ArrayList<ArrayList<Tile>> tempTable = new ArrayList<ArrayList<Tile>>(((Table)table).getTable());
		
		int tempTableTilesSize = tempTableTiles.size();
		int tempTableSize = tempTable.size();
		
		for(int i = 0; i < tempTableSize; i++) {
			((Table)table).set(i,tableTiles.get(i));
		}
		
		for(int i = tempTableSize; i < tempTableTilesSize; i++) {
			((Table)table).addMeldToTable(tableTiles.get(i));
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
}
