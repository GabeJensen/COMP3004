package core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import GUI.GUI;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;
import observer.Game;

public class TileRummyGame {
	private final int penalty = 3; //Num tiles to draw when end of turn, table is invalid
	private Game game;
	private Player user;
	private Player p1;
	private Player p2;
	private Player p3;
	private ArrayList<Player> players;
	private Queue<Player> orderedPlayers;
	private Player currentPlayer;
	private Deck deck;
	private Originator originator;
	private Caretaker caretaker;
	private boolean emptyDeck;
	
	//TODO: "I" will change this to 120 when it is all figured out.
	private final int turnDuration = 30;
	private int currentTurnTime;
	
	Timeline time_sched = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent ev) {
			currentTurnTime--;
			GUI.updateTime(currentTurnTime);
			timeEnd();
		}
		public void timeEnd() {
			if (currentTurnTime == 0) {
				playerPenalty();
				GUI.updateDisplayHand(currentPlayer.getTiles());
				GUI.displayToConsole(currentPlayer.getName() + " ran out of time! The penalty was applied and " + currentPlayer.getName() + " drew 3 tiles!");
			}
		}
	}));
	
	public static void main(String[] args) {
		javafx.application.Application.launch(GUI.class);
	}
	
	@Deprecated
	public void initalizeGame() {
		game = new Game();
		originator = new Originator();
		caretaker = new Caretaker();
		user = new Player(game, "User", new Strat0());
		p1 = new Player(game, "P1", new Strat1());
		p2 = new Player(game, "P2", new Strat2());
		p3 = new Player(game, "P3", new Strat3());
		deck = new Deck();
		deck.shuffleDeck();
		emptyDeck = false;
		game.setPlayers(user, p1, p2, p3);
	}
	
	public void initalizeGame(int userCount, ArrayList<String> strategySelection) {
		game = new Game();
		originator = new Originator();
		caretaker = new Caretaker();
		players = new ArrayList<Player>();
		deck = new Deck();
		deck.shuffleDeck();
		emptyDeck = false;
		//Goes through all humans and names them
		for(int i = 0; i < userCount; i++) {
			players.add(new Player(game, "User " + (i+1), new Strat0()));
		}
		
		//Goes throgh al strategies and names them appropriately
		for(int i = 0; i < strategySelection.size(); i++) {
			String selection = strategySelection.get(i);
			String name = "P" + (i+1);
			if(selection.endsWith("1")) {
				players.add(new Player(game, name, new Strat1()));				
			} else if (selection.endsWith("2")) {
				players.add(new Player(game, name, new Strat2()));
			} else if (selection.endsWith("3")) {
				players.add(new Player(game, name, new Strat3()));
			} else if (selection.endsWith("4")) {
				// Strategy 4...
			}
		}

		game.setPlayers(players);
	}
	
	public void displayOtherUserHands() {
		for(Player player : players) {
			if(currentPlayer != player) {
				GUI.displayToConsole(player.getName() + "'s hand: " + player.getHand());
			}
		}
		GUI.displayToConsole("----------");
	}
	
	public void playGame() {
		// deal 14 tiles to each player
		for (Player player : players) {
			for (int c = 0; c < 14; c++) {
				player.addTile(deck.dealTile());
			}
		}
		
		orderedPlayers = decideOrder(players.size());		
		nextTurn();
	}
	
	private LinkedList<Player> decideOrder(int playerCount) {
		Deck orderDeck = new Deck();
		orderDeck.shuffleDeck();
		
		ArrayList<int[]> ordering = new ArrayList<int[]>();
		ArrayList<Integer> values = new ArrayList<Integer>();
		
		int player = 0;
		int user = 0;
		
		// Loop for however many players, 0 being first player, 1 the 2nd, etc.
		for (int i = 0; i < playerCount; i++) {
			int v = orderDeck.dealTile().getValue();
			//TODO: Handle when a Joker is drawn here.
			// should just be adding a condition if joker's value for example were "*" or -1 to this while loop below
			while (values.contains(v)) {
				v = orderDeck.dealTile().getValue();
			}
			if (players.get(i).getName().contains("User")) {
				user++;
				GUI.displayToConsole("User " + (user) + " has drawn a tile with a value of " + v + "!");
			}
			else {
				player++;
				GUI.displayToConsole("Player " + (player) + " has drawn a tile with a value of " + v + "!");
			}
			ordering.add(new int[] {i, v});
			values.add(v);
		}
		
		List<Integer> playerOrder = new ArrayList<Integer>(players.size());
		
		while (ordering.size() > 0) {
			playerOrder.add(max(ordering));
		}
		
		LinkedList<Player> drawnPlayers = new LinkedList<Player>();
		
		for (int i = 0; i < playerOrder.size(); i++) {
			drawnPlayers.add(players.get(playerOrder.get(i)));
		}
		
		return drawnPlayers;
	}
	
	private int max(ArrayList<int[]> order) {
		int max = Integer.MIN_VALUE;
		int maxIndex = -1;
		int playerIndex = -1;
		for (int x = 0; x < order.size(); x++) {
			if (order.get(x)[1] > max) {
				max = order.get(x)[1];
				playerIndex = order.get(x)[0];
				maxIndex = x;
			}
		}
		order.remove(maxIndex);
		return playerIndex;
	}
	
	private void next() {
		//Assigns current player to next player in line and puts them at the back creating a circular queue
		currentPlayer = orderedPlayers.remove();
		orderedPlayers.add(currentPlayer);
	}
	
	private void startTimer(Timeline t, int duration) {
		this.currentTurnTime = duration;
		t.setCycleCount(duration);
		t.play();
	}
	
	private void stopTimer(Timeline t) {
		t.stop();
	}
	
	private void nextTurn() {
		/*int turnValue;
		Tile tile;*/
		
		next();
		/*New Idea
		 * Instead of loop we have a nextTurn function
		 * Goes to next player in line and performs their action
		 * if human do human stuff else do ai stuff
		 * AI stuff we have implemented more or less
		 * On human side we just update the playArea and have whatever user play their hand and end turn
		 * NOTE: I am not sure how the GUI handles what player is playing and if the tiles in the play area would 
		 * properly reflect the player who's tiles those belong to
		 * */
		
		GUI.updateTime(turnDuration);
		
		if(currentPlayer.getName().contains("User")) {
			stopTimer(time_sched);
			startTimer(time_sched, turnDuration);
			
			//User stuff
			GUI.updateDisplayHand(currentPlayer.getTiles());
			
			originator.setState(game.getTable(), currentPlayer.getTiles());
			caretaker.add(currentPlayer.getName(), originator.saveMemento());
			GUI.displayToConsole(currentPlayer.getName() + "'s turn!");
			displayOtherUserHands();
			
			//Needs to wait for end turn input
		} else {
			//Ai stuff
			aiTurn();
			nextTurn();
		}
	}
	
	private void aiTurn() {
		int turnValue;
		Tile tile;
		turnValue = currentPlayer.performStrategy();
		if (turnValue == 0) {
			if(!emptyDeck) {
				tile = deck.dealTile();
				if(tile == null) {
					emptyDeck = true;
					GUI.displayToConsole(currentPlayer.getName() + " tried drawing, but the deck was empty!");
				} else {
					currentPlayer.addTile(tile);
					GUI.displayToConsole(currentPlayer.getName() + " draws " + tile.toString() + "!" );
				}
			} else {
				GUI.displayToConsole(currentPlayer.getName() + " can't draw because the deck is empty!");
			}
			GUI.displayToConsole(currentPlayer.getName() + "'s hand: " + currentPlayer.getHand());
		}
		else if (turnValue == 1) {
			GUI.displayToConsole(currentPlayer.getName() + " played this turn!");
			if(currentPlayer.getHandCount() == 0) {
				GUI.displayToConsole(currentPlayer.getName() + " says: 'RUMMIKUB!' They won the game!");
				GUI.disableButtons();
			} else {
				GUI.displayToConsole(currentPlayer.getName() + "'s hand: " + currentPlayer.getHand());				
			}
		}
		
		GUI.updateDisplayTable(game.getTable());
	}
	
	@Deprecated
	public void gameLoop() {
		Player[] nonHumanPlayers = {p1, p2, p3};
		int turnValue;
		Tile tile;
		for (Player p : nonHumanPlayers) {
			turnValue = p.performStrategy();
			if (turnValue == 0) {
				if(!emptyDeck) {
					tile = deck.dealTile();
					if(tile == null) {
						emptyDeck = true;
						GUI.displayToConsole(p.getName() + " tried drawing, but the deck was empty!");
					} else {
						p.addTile(tile);
						GUI.displayToConsole(p.getName() + " draws " + tile.toString() + "!" );
					}
				} else {
					GUI.displayToConsole(p.getName() + " can't draw because the deck is empty!");
				}
				GUI.displayToConsole(p.getName() + "'s hand: " + p.getHand());
				continue;
			}
			else if (turnValue == 1) {
				GUI.displayToConsole(p.getName() + " played this turn!");
				if(p.getHandCount() == 0) {
					GUI.displayToConsole(p.getName() + " says: 'RUMMIKUB!' They won the game!");
					GUI.disableButtons();
					break;
				}
				GUI.displayToConsole(p.getName() + "'s hand: " + p.getHand());
				continue;
			}
		}		
		
		GUI.updateDisplayHand(currentPlayer.getTiles());
		GUI.updateDisplayTable(game.getTable());
		originator.setState(game.getTable(), currentPlayer.getTiles());
		caretaker.add(currentPlayer.getName(), originator.saveMemento());
	}
	
	public int endTurn(ArrayList<Tile> currentTurnUserUsedTiles, List<ArrayList<Tile>> currentTurnMelds, List<ArrayList<Tile>> turnTableState, int numItemsInPlayMeldBox) {
		/**
		 * Function returns -1 if there are tiles in the "playMeldBox", otherwise returns 0.
		 */
		boolean appliedPlayerPenalty = false;
		
		// if there are still tiles in the "playMeldBox"
		if (numItemsInPlayMeldBox > 1) {
			GUI.displayToConsole("There are still tiles you are trying to play as a meld!");
			return -1;
		}
		
		// If the user didn't play anything this turn.
		if ((currentTurnMelds.isEmpty()) && currentTurnUserUsedTiles.isEmpty()) {
			if(!emptyDeck) {
				Tile draw = deck.dealTile();
				if(draw == null) {
					emptyDeck = true;
					GUI.displayToConsole(currentPlayer.getName() + " tried drawing, but the deck was empty!");
				} else {
					GUI.displayToConsole(currentPlayer.getName() + " draws " + draw.toString() + "!");
					currentPlayer.addTile(draw);						
				}
			} else {
				GUI.displayToConsole(currentPlayer.getName() + " tried drawing, but the deck was empty!");
			}
		} else {
			// If the user has not played their initial 30 meld yet
			if (!currentPlayer.getInit30Flag()){
				// No table tiles allowed to be played until the initial 30 meld(s)
				int tileCount = 0;
				for (ArrayList<Tile> meld : currentTurnMelds) {
					for (Tile t : meld) {
						tileCount++;
					}
				}
				// If the tile count of hand melds is the same as the count of total tiles used, it means that only hand melds were played
				if (tileCount == currentTurnUserUsedTiles.size()) {
					// User has to play HAND melds that add up to 30 or more and nothing else
					int handMeldSum = 0;
					for (ArrayList<Tile> meld : currentTurnMelds) {
						handMeldSum += Meld.getValue(meld);
					}
					if (handMeldSum < 30) {
						GUI.displayToConsole("The value of the hand melds played do not add up to at least 30 points!");
						playerPenalty();
						appliedPlayerPenalty = true;
					} else {
						currentPlayer.playedInit30();
					}
				} else {
					// Current player attempted to re-use tiles on the table before playing their initial 30 points
					GUI.displayToConsole("You must play the initial greater than 30 valued hand meld(s) first before using tiles on the table!");
					playerPenalty();
					appliedPlayerPenalty = true;
				}
			}
		}
		
		// After checking all rules and no player penalty has been applied, check if the table state is valid
		// Note: This if-statement is to prevent penalizing the current player twice (e.g., haven't played initial 30 but, 
		// 			played meld(s) using table tiles and left the table in an invalid state)
		if (!appliedPlayerPenalty) {
			// Check if the current state of the table is valid
			for (ArrayList<Tile> meld: turnTableState) {
				if (!Meld.checkValidity(meld)) {
					GUI.displayToConsole("The table had some invalid melds!");
					playerPenalty();
					appliedPlayerPenalty = true;
					break;
				}
			}
		}
		
		// At this point, all checking is complete
		if (appliedPlayerPenalty) {
			// display the restored table
			// Note: the restored table happens in playerPenalty()
			GUI.updateDisplayTable(game.getTable());
		} else {
			// Remove the user used tiles from user hand
			for (int i = 0; i < currentTurnUserUsedTiles.size(); ++i) {
				currentPlayer.removeTile(currentTurnUserUsedTiles.get(i));
			}
			game.setTable(turnTableState);
		}
		
		// Check if human player on this turn has won or not
		if(currentPlayer.getHandCount() == 0) {
			GUI.displayToConsole(currentPlayer.getName() + " says: 'RUMMIKUB!' They won the game!");
			GUI.disableButtons();
		} else {
			nextTurn();
		}
		stopTimer(time_sched);
		startTimer(time_sched, turnDuration);
		return 0;
	}
	
	private void playerPenalty() {
		originator.restoreMemento(caretaker.get(currentPlayer.getName()));
		
		game.setTable(originator.getState().getTable());
		
		// deal "penalty" number of tiles to current player for invalid moves
		ArrayList<Tile> startingHand = originator.getState().getHand();
		for (int i = 0; i < penalty; ++i) {
			Tile t = deck.dealTile();
			if (t == null) {
				emptyDeck = true;
				GUI.displayToConsole(currentPlayer.getName() + " tried drawing due to penalty, but the deck was empty!");
				break;
			} else {
				startingHand.add(t);
				GUI.displayToConsole(currentPlayer.getName() + " draws " + t.toString() + " due to penalty!" );
			}
		}
		
		Collections.sort(startingHand, new TileComparator());
		currentPlayer.setTiles(startingHand);
	}
}
