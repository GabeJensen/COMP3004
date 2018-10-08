package core;

public class Player {
	private Hand hand = new Hand();
	private String name;
			
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
