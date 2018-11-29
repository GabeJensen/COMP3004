package core;

import java.util.Comparator;
import java.util.HashMap;

public class Tile {
	private String color;
	private String value;
	
	public boolean handPlayed;
	public boolean tablePlayed;
	private boolean isJoker;
	
	public Tile(String c, String v) {
		handPlayed = false;
		tablePlayed = false;
		isJoker = false;
		String[] validColors = {"R", "B", "G", "O"};
		c = c.toUpperCase();
		boolean validFlag = false;
		
		for (int i = 0; i < validColors.length; i++) {
			// If the passed in color is any one of the valid colors, then it is okay and breaks out of the loop.
			if (c.equals(validColors[i])) {
				validFlag = true;
				break;
			}
		}
		
		//Checks for Joker
		if(v.equals("J")) {
			this.value = "-1";
			isJoker = true;
		} else {
			try {
				int intV;
				intV = Integer.parseInt(v);
				if ((intV > 0) || (intV < 14)) {
					this.value = v;
				}
				else {
					throw new IllegalArgumentException("Invalid value out of range: " + c);
				}
			}
			catch (NumberFormatException e) {
				throw e;
			}		
		}
		
		if (validFlag) {
			this.color = c;
//			this.value = v;
		}
		else {
			throw new IllegalArgumentException("Invalid color: " + c);
		}	
	}
	
	private Tile() {
		this.handPlayed = false;
		this.tablePlayed = false;
	}
	
	public Tile copyTile(Tile t) {
		String[] copyInfo = t.getInfo();
		Tile newTile = new Tile(copyInfo[0], copyInfo[1]);
		return newTile;
	}
	
	public boolean setValue(String v) {
		//Only works if it's a joker
		if(isJoker) {
			try {
				int intV;
				intV = Integer.parseInt(v);
				//Can be set back to a Joker (-1)
				if (((intV > 0) && (intV < 14)) || intV == -1) {
					this.value = v;
					return true;
				}

			}
			catch (NumberFormatException e) {
				throw e;
			}
		} 
		return false;
	}
	
	public boolean setValue(Integer v) {
		return setValue(Integer.toString(v));
	}
	
	public boolean setColor(String c) {
		//Only works if it is a Joker
		if(isJoker) {
			String[] validColors = {"R", "B", "G", "O"};
			c = c.toUpperCase();
			for(String color : validColors) {
				if(color.equals(c)) {
					this.color = c;
					return true;
				}
			}
			
		}
		
		return false;
	}
	
	public boolean isJoker() {
		return isJoker;
	}
	
	@Override
	public String toString() {
		if(isJoker) {
			return "J";
		}
		return color + value;
	}
	
	public int getValue() {
		return Integer.parseInt(value);
	}
	
	public String[] getInfo() {
		if(isJoker) {
			return new String[] {color, "J"};
		}
		return new String[] {color, value};
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		
		if (!(o instanceof Tile)){
			return false;
		}
		
		Tile t = (Tile) o;
		if(isJoker && t.isJoker) {
			return true;
		}
		if (this.color.equals(t.color) && this.value.equals(t.value)) {
			return true;
		}else {
			return false;
		}
	}
}

class TileComparator implements Comparator<Tile> {
	public int compare(Tile t1, Tile t2) {
		//If either tile are jokers then the comparison will fall in this if statement
		//Jokers are always less than regular tiles
		if(t1.isJoker()) {
			if(t2.isJoker()) {//Both Jokers
				return 0;
			} else {
				return 1;
			}
		} else if(t2.isJoker()) {
			return -1;
		}
		
		HashMap<String, Integer> conv = new HashMap<String, Integer>();
		conv.put("R", 4);
		conv.put("B", 3);
		conv.put("G", 2);
		conv.put("O", 1);
		
		int color1 = conv.get(t1.getInfo()[0]);
		int color2 = conv.get(t2.getInfo()[0]);
		int val1 = Integer.parseInt(t1.getInfo()[1]);
		int val2 = Integer.parseInt(t2.getInfo()[1]);
		
		// If Tile 1 is greater than Tile 2
		if (color1 > color2) {
			return -1;
		}
		// If Tile 1 color equals Tile 2 color
		else if (color1 == color2) {
			// If Tile 1 value is greater than Tile 2 value
			if (val1 > val2) {
				return 1;
			}
			// If values are same
			else if (val1 == val2) {
				return 0;
			}
			// If Tile 1 value is lesser than Tile 2 vale
			else {
				return -1;
			}
		}
		// If Tile 1 is lesser than Tile 2
		else { // color1 < color2
			return 1;
		}
	}
}
