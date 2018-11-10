package core;

import java.util.HashMap;

public class Caretaker {
	private HashMap<String, Memento> mementos = new HashMap<String, Memento>();
	
	public void add(String player, Memento state) {
		this.mementos.put(player, state);
	}
	
	public Memento get(String player) {
		return this.mementos.get(player);
	}
}
