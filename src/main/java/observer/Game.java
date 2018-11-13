package observer;

import java.util.ArrayList;
import java.util.List;

import core.Tile;
import core.Meld;
import core.Player;

public class Game implements Subject {
	private List<Observer> observers;
	private List<ArrayList<Tile>> table;
	private Player p1;
	private Player p2;
	private Player p3;
	private Player p4;
	private ArrayList<Player> activePlayers;
	
	public Game(Player user, Player p1, Player p2, Player p3) {
		this.observers = new ArrayList<Observer>();
		this.table = new ArrayList<ArrayList<Tile>>();
		this.p4 = user;
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
	}
	
	public Game() {
		this.observers = new ArrayList<Observer>();
		this.table = new ArrayList<ArrayList<Tile>>();
		this.p1 = null;
		this.p2 = null;
		this.p3 = null;
		this.p4 = null;
		this.activePlayers = new ArrayList<Player>();
	}
	
	@Override
	public void registerObserver(Observer o) {
		observers.add(o);
	}

	@Override
	public void unregisterObserver(Observer o) {
		observers.remove(o);
	}

	@Override
	public void notifyObservers() {
		for (int i = 0; i < observers.size(); ++i){
			Observer obsvr = (Observer)observers.get(i);
			obsvr.update();
		}
	}
	
	@Deprecated
	public void setPlayers(Player user, Player p1, Player p2, Player p3) {
		this.p4 = user;
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
	}
	
	public void setPlayers(ArrayList<Player> players) {
		p1 = players.get(0);
		activePlayers.add(p1);
		p2 = players.get(1);
		activePlayers.add(p2);
		if(players.size() > 2) {
			p3 = players.get(2);
			activePlayers.add(p3);
		}
		if(players.size() > 3) {
			p4 = players.get(3);
			activePlayers.add(p4);
		}
	}
	
	public List<ArrayList<Tile>> getTable(){
		return table;
	}
	
	public int getSmallestHandSize() {
		// returns the smallest hand size in the game
		int totalMin = Integer.MAX_VALUE;
		
		for (Player player : activePlayers) {
			totalMin = Math.min(player.getHandCount(), totalMin);
		}
		return totalMin;
	}
	
	public ArrayList<Tile> setMeldOnTable(int index, ArrayList<Tile> setMeld){
		if(index >= table.size()) {
			return null;
		}
		ArrayList<Tile> replaced = table.set(index,setMeld);
		notifyObservers();
		return replaced;
	}

	public boolean addMeldToTable(ArrayList<Tile> meldToAdd) {
		/**
		 * Returns True if the meldToAdd is a valid meld. Otherwise returns False.
		 */
		 if (Meld.checkValidity(meldToAdd)) {
			 table.add(meldToAdd);
			 notifyObservers();
			 return true;
		 }else {
			 // tried to add an invalid meld to table
			 return false;
		 }
	}

	public void setTable(List<ArrayList<Tile>> newTable) {
		this.table = newTable;
		notifyObservers();
	}
}
