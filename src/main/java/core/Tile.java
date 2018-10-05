package core;

public class Tile {
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
				value = v;
			}
			else {
				throw new IllegalArgumentException("Invalid value out of range: " + c);
			}
		}
		catch (NumberFormatException e) {
			throw e;
		}
		
		if (validFlag) {
			color = c;
			value = v;
		}
		else {
			throw new IllegalArgumentException("Invalid color: " + c);
		}
	}
	
	public String[] getInfo() {
		return new String[] {color, value};
	}

}
