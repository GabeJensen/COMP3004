package core;

import java.util.ArrayList;
import java.util.List;

public interface PlayerStrategy {
	public int strat(Hand h, boolean initFlag, List<ArrayList<Tile>> table);
}
