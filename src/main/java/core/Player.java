package core;

public class Player {
	Hand hand = new Hand();
	String name;
			
	public Player() {
		this.name = "No Name";
	}
	
	public Player(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
