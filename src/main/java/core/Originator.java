package core;

import java.util.ArrayList;
import java.util.List;

public class Originator {
	private Memento state;
	
	public void setState(List<ArrayList<Tile>> table, ArrayList<Tile> hand) {
		this.state = new Memento(table, hand);
	}
	
	public Memento getState() {
		return this.state;
	}
	
	public Memento saveMemento() {
		return state.getMemento();
	}
	
	public void restoreMemento(Memento m) {
		this.state = m.getMemento();
	}
}
