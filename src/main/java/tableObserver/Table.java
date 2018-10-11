package tableObserver;

import java.util.ArrayList;
import java.util.List;

import core.Tile;

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

}
