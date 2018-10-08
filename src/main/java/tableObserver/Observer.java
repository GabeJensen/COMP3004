package tableObserver;

import java.util.ArrayList;

import core.Meld;

public interface Observer {
	public void update(ArrayList<Meld> meldsOnTable);
}
