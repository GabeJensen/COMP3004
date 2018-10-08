package core;

import java.util.Comparator;
import java.util.HashMap;

public class Tile {
	@Override
	public String toString() {
		return color + value;
	}

	private String color;
	private String value;
	
	public Tile(String c, String v) {
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
		
		if (validFlag) {
			this.color = c;
			this.value = v;
		}
		else {
			throw new IllegalArgumentException("Invalid color: " + c);
		}
	}
	
	public String[] getInfo() {
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
		if (this.color.equals(t.color) && this.value.equals(t.value)) {
			return true;
		}else {
			return false;
		}
	}
}

class TileComparator implements Comparator<Tile> {
	public int compare(Tile t1, Tile t2) {
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
