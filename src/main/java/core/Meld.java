package core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class Meld {
	ArrayList<Tile> meld = new ArrayList<Tile>();
	
	public Meld() {
		
	}
	
	public boolean createMeld(ArrayList<Tile> newMeld) {
		meld = newMeld;
		
		return checkValidity();
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
			if(suit.isEmpty()) {
				suit = tileStr[0];
			} else if(suit != tileStr[0]) {
				return false;
			}
		}
		
		Collections.sort(values);
		
		int lastValue = -1;
		for (Integer integer : values) {
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
			if(existingSuit.contains(color)) {
				return false;
			}
			existingSuit.add(color);
			if(value.isEmpty()) {
				value = tileStr[1];
			} else if(value != tileStr[1]) {
				return false;
			}
			
		}
		return true;
	}
}
