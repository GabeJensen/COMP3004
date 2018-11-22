package core;

import java.util.ArrayList;
import java.util.List;

public class Memento {
	private List<ArrayList<Tile>> turn_start_table;
	private ArrayList<Tile> turn_start_hand;
	
	public Memento(List<ArrayList<Tile>> startTable, ArrayList<Tile> startUserHand) {
		this.turn_start_table = meldContainerCopy(startTable);
		this.turn_start_hand = tileLevelCopy(startUserHand);
	}
	
	public List<ArrayList<Tile>> getTable() {
		return turn_start_table;
	}
	
	public ArrayList<Tile> getHand() {
		return turn_start_hand;
	}
	
	protected Memento getMemento() {
		return new Memento(this.turn_start_table, this.turn_start_hand);
	}
	
	private ArrayList<Tile> tileLevelCopy(ArrayList<Tile> tiles) {
		ArrayList<Tile> tileCopy = new ArrayList<Tile>();
		
		for (Tile t : tiles) {
			tileCopy.add(t.copyTile(t));
		}
		
		return tileCopy;
	}
	
	private List<ArrayList<Tile>> meldContainerCopy(List<ArrayList<Tile>> melds) {
		List<ArrayList<Tile>> meldsCopy = new ArrayList<ArrayList<Tile>>();
		
		for (ArrayList<Tile> m : melds) {
			meldsCopy.add(tileLevelCopy(m));
		}
		
		return meldsCopy;
	}
}
