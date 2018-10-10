package core;

public class Player {
	private Hand hand;
	private String name;
			
	public Player() {
		this.hand = new Hand();
		this.name = "No Name";
	}
	
	public Player(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
