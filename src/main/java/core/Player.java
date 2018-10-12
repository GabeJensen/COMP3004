package core;

import java.util.ArrayList;
import java.util.List;

import tableObserver.Observer;
import tableObserver.Subject;

public class Player implements Observer {
	private Hand hand;
	private String name;
	private boolean initial30;
	private Subject table;
			
	public Player(Subject t) {
		this.hand = new Hand();
		this.name = "No Name";
		this.initial30 = false;
		this.table = t;
		t.registerObserver(this);
	}
	
	public Player(Subject t, String name) {
		this.name = name;
		this.initial30 = false;
		this.table = t;
	}
	
	public void update(List<ArrayList<Tile>> tableMelds) {
		// Player will use this table in some way, either to play hand melds and/or table melds.
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
