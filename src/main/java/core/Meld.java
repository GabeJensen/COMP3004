package core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class Meld {
	public static int getValue(ArrayList<Tile> meld) {
		int value = 0;
		for (int i = 0; i < meld.size(); i++) {
			value += meld.get(i).getValue();
		}
		return value;
	}

	public static boolean checkValidity(ArrayList<Tile> meld) {
		//Checks for minimum and maximum meld sizes (max possible with jokers)
		if(meld.size() < 3 || meld.size() > 13) {
			return false;
		}
		
		//Check for run
		//Check for set
		//If they both fail then it's not a meld. It's impossible that they are both are true
		if(!set(meld) && !run(meld)) {
			return false;
		}
		return true;
	}
	
	//Function is meant for finding the first value when jokers are first tiles in a meld
	private static int checkForFirstNonJokerValue(ArrayList<Tile> tiles) {
		int count = 0;
		for(Tile tile : tiles) {
			if(!tile.isJoker()) {
				return tile.getValue() - count;
			}
			count++;
		}
		return 0;
	}

	private static boolean run(ArrayList<Tile> meld) {
		//Same suit ordered value
		String suit = "";
		String[] tileStr;
		ArrayList<Integer> values = new ArrayList<>();
		Queue<Tile> jokers = new LinkedList<Tile>(); //Used later to set the suit
		Tile tile;
		Queue<Tile> tiles = new LinkedList<>(meld);
		
		int lastValue = -1;
		int value;
		while(!tiles.isEmpty()) {
			tile = tiles.remove();
			//If tile is a joker then set its value to the next one and continue the loop
			if(tile.isJoker()) {
				jokers.add(tile);
				if(values.isEmpty()) {
					value = checkForFirstNonJokerValue(meld);
					if(value < 1) {
						return false;
					} else {
						tile.setValue(value);						
					}
				} else {
					tile.setValue(Integer.toString(values.get((values.size()-1)) + 1));
				}
				//Jokers ignore suit for now
				values.add(tile.getValue());
			} else {
				tileStr = tile.getInfo();
				values.add(tile.getValue());
				//Checks all tile suit's against the first suit
				if(suit.isEmpty()) {
					suit = tileStr[0];
				} else if(!suit.equals(tileStr[0])) {
					return false;
				}				
			}
			
		}
		
		//Set Joker color
		for(Tile j : jokers) {
			j.setColor(suit);
		}
		
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

	private static boolean set(ArrayList<Tile> meld) {
		//Same value different suit
		HashSet<String> existingSuit = new HashSet<>();
		String[] validColors = {"R", "B", "G", "O"};
		String value = "";
		String color;
		String[] tileStr;
		ArrayList<Tile> jokers = new ArrayList<Tile>();
		
		for (Tile tile : meld) {
			if(tile.isJoker()) {
				jokers.add(tile);
				continue;
			}
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
			} else if(!value.equals(tileStr[1])) {
				return false;
			}
			
		}
		
		for(Tile joker : jokers) {
			//Since it continues in the previous loop when the tile is a joker. 
			//It is possible they give in 5 or 6 tiles and the prev loop will work fine
			if(existingSuit.size() == 4) {
				return false;
			}
			for(String c : validColors) {
				if(!existingSuit.contains(c)) {
					joker.setColor(c);
					joker.setValue(value);
					break;
				}
			}
			
		}
		return true;
	}
}
