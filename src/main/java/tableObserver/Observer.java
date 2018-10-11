package tableObserver;

import java.util.ArrayList;
import java.util.List;

import core.Tile;

public interface Observer {
	public void update(List<ArrayList<Tile>> meldsOnTable);
}
