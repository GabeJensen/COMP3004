package core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class Meld {
	ArrayList<Tile> meld; 
	
	public Meld() {
		meld = new ArrayList<Tile>();
	}
	
	public boolean createMeld(ArrayList<Tile> newMeld) {
		meld = newMeld;
		
		return checkValidity();
	}
	
	public int getValue() {
		int value = 0;
		for (int i = 0; i < meld.size(); i++) {
			value += meld.get(i).getValue();
		}
		return value;
	}

	private boolean checkValidity() {
		if(meld.size()<3) {
			return false;
		}
		
		//Check for run
		//Check for set
		if(!set() && !run()) {
			return false;
		}
		return true;
	}

	private boolean run() {
		//Same suit ordered value
		String suit = "";
		String[] tileStr;
		ArrayList<Integer> values = new ArrayList<>();
		
		for (Tile tile : meld) {
			tileStr = tile.getInfo();
			values.add(Integer.valueOf(tileStr[1]));
			//Checks all tile suit's against the first suit
			if(suit.isEmpty()) {
				suit = tileStr[0];
			} else if(suit != tileStr[0]) {
				return false;
			}
		}
		
		int lastValue = -1;
		for (Integer integer : values) {
			//Check that the next value is one greater than the last value
			if(lastValue == -1) {
				lastValue = integer;
			} else if(lastValue + 1 != integer) {
				return false;
			}
			
			lastValue = integer;
		}
		
		return true;
	}

	private boolean set() {
		//Same value different suit
		HashSet<String> existingSuit = new HashSet<>();
		String value = "";
		String color;
		String[] tileStr;
		
		for (Tile tile : meld) {
			tileStr = tile.getInfo();
			color = tileStr[0];
			//If the suit already exists then the meld is invalid
			if(existingSuit.contains(color)) {
				return false;
			}
			existingSuit.add(color);
			//Set the value from the first tile and compare with every other tile
			if(value.isEmpty()) {
				value = tileStr[1];
			} else if(value != tileStr[1]) {
				return false;
			}
			
		}
		return true;
	}
}
