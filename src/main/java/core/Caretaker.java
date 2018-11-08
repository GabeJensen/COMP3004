package core;

import java.util.ArrayList;

public class Caretaker {
	private ArrayList<Memento> mementos = new ArrayList<Memento>();
	
	public void add(Memento state) {
		this.mementos.add(state);
	}
	
	public Memento get(int i) {
		return this.mementos.get(i);
	}
}
