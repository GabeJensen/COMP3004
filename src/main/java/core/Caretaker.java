package core;

public class Caretaker {
	// Only one state now. Normally is a list...
	private Memento start_of_turn;
	
	public void set(Memento state) {
		this.start_of_turn = state;
	}
	
	public Memento get() {
		return this.start_of_turn;
	}
}
