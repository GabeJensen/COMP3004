package tableObserver;

import java.util.ArrayList;
import java.util.List;

import core.Tile;
import core.Meld;

public class Table implements Subject {
	private List<Observer> observers;
	private List<ArrayList<Tile>> meldsOnTable;
	
	public Table() {
		this.observers = new ArrayList<Observer>();
		this.meldsOnTable = new ArrayList<ArrayList<Tile>>();
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
			obsvr.update(meldsOnTable);
		}
	}
	
	public List<ArrayList<Tile>> getTable(){
		return meldsOnTable;
	}
	
	public ArrayList<Tile> set(int index, ArrayList<Tile> setMeld){
		if(index >= meldsOnTable.size()) {
			return null;
		}
		return meldsOnTable.set(index,setMeld);
	}

	public boolean addMeldToTable(ArrayList<Tile> meldToAdd) {
		/**
		 * Returns True if the meldToAdd is a valid meld. Otherwise returns False.
		 */
		 if (Meld.checkValidity(meldToAdd)) {
			 meldsOnTable.add(meldToAdd);
			 notifyObservers();
			 return true;
		 }else {
			 // tried to add an invalid meld to table
			 return false;
		 }
	}

}
