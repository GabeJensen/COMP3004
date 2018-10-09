package observer;

import java.util.ArrayList;

import core.Meld;

public class Table implements Subject {
	private ArrayList<Observer> observers;
	private ArrayList<Meld> meldsOnTable;
	
	public Table() {
		this.observers = new ArrayList<Observer>();
		this.meldsOnTable = new ArrayList<Meld>();
	}
	
	@Override
	public void registerObserver(Observer o) {
		this.observers.add(o);
	}

	@Override
	public void unregisterObserver(Observer o) {
		this.observers.remove(o);
	}

	@Override
	public void notifyObservers() {
		for (int i = 0; i < observers.size(); ++i){
			Observer obsvr = (Observer)observers.get(i);
			obsvr.update();
		}
	}

}
