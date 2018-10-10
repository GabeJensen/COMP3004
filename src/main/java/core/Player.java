package core;

public class Player {
	private Hand hand;
	private String name;
	private boolean initial30;
			
	public Player() {
		this.hand = new Hand();
		this.name = "No Name";
		this.initial30 = false;
	}
	
	public Player(String name) {
		this.name = name;
		this.initial30 = false;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean getInit30Flag() {
		return this.initial30;
	}
	
	public void playedInit30() {
		this.initial30 = true;
	}
}
