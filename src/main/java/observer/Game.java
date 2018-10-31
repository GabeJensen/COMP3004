package observer;

import java.util.ArrayList;
import java.util.List;

import core.Tile;
import core.Meld;
import core.Player;

public class Game implements Subject {
	private List<Observer> observers;
	private List<ArrayList<Tile>> table;
	private Player user;
	private Player p1;
	private Player p2;
	private Player p3;
	
	public Game(Player user, Player p1, Player p2, Player p3) {
		this.observers = new ArrayList<Observer>();
		this.table = new ArrayList<ArrayList<Tile>>();
		this.user = user;
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
	}
	
	public Game() {
		this.observers = new ArrayList<Observer>();
		this.table = new ArrayList<ArrayList<Tile>>();
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
	
	public void setPlayers(Player user, Player p1, Player p2, Player p3) {
		this.user = user;
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
	}
	
	public List<ArrayList<Tile>> getTable(){
		return table;
	}
	
	public int getSmallestHandSize() {
		// returns the smallest hand size in the game
		if (user == null || p1 == null || p2 == null || p3 == null) {
			return -1;
		} else {
			return Math.min(user.getHandCount(), Math.min(p1.getHandCount(), Math.min(p2.getHandCount(), p3.getHandCount())));
		}
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
}
