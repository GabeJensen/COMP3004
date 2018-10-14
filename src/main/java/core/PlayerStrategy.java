package core;

import java.util.ArrayList;
import java.util.List;

public interface PlayerStrategy {
	public void strat(Hand h, boolean initFlag, List<ArrayList<Tile>> table);
}
