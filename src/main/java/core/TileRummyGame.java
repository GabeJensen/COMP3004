package core;

import java.util.ArrayList;
import java.util.List;

import GUI.MainScreen;
import observer.Game;

public class TileRummyGame {
	private static MainScreen gui;
	private Game game;
	private Player user;
	private Player p1;
	private Player p2;
	private Player p3;
	private Deck deck;
	private Originator originator;
	private Caretaker caretaker;
	private boolean emptyDeck;
	
	public static void main(String[] args) {
		javafx.application.Application.launch(MainScreen.class);
	}
	
	public void initalizeGame() {
		game = new Game();
		originator = new Originator();
		caretaker = new Caretaker();
		user = new Player(game, "User", new Strat0());
		p1 = new Player(game, "P1", new Strat1());
		p2 = new Player(game, "P2", new Strat2());
		p3 = new Player(game, "P3", new Strat3());
		game.setPlayers(user, p1, p2, p3);
	}
	
	public void playGame() {
		//Create and initialize GUI
		/* Create buttons for:
		 * - Starting the game
		 * - Reseting table to start of turn
		 * - Ending your turn (checking melds)
		 * */
		
		//Create deck
		deck = new Deck();
		deck.shuffleDeck();
		emptyDeck = false;
		
		//Set tiles for users using deck
		Player[] allPlayers = {user, p1, p2, p3};
		
		for (Player player : allPlayers) {
			for (int c = 0; c < 14; c++) {
				player.addTile(deck.dealTile());
			}
		}
		
		// Init the user's temporary display meld array
		MainScreen.currentTurnMelds = new ArrayList<ArrayList<Tile>>();
		MainScreen.currentTurnUserUsedTiles = new ArrayList<Tile>();
		MainScreen.tablePlayTiles = new ArrayList<Tile>();
		
		MainScreen.updateDisplayHand(user.getTiles());
		MainScreen.updateDisplayTable(game.getTable());
		
		// Because apparently we need to display everyone's hand at the start as well.
		MainScreen.displayToConsole("P1's hand: " + p1.getHand());
		MainScreen.displayToConsole("P2's hand: " + p2.getHand());
		MainScreen.displayToConsole("P3's hand: " + p3.getHand());
		
		
		// Start of the game. We still want to be able to revert here.
		originator.setState(game.getTable(), user.getTiles());
		caretaker.add(user.getName(), originator.saveMemento());
	}
	
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
						MainScreen.displayToConsole(p.getName() + " tried drawing, but the deck was empty!");
					} else {
						p.addTile(tile);
						MainScreen.displayToConsole(p.getName() + " draws " + tile.toString() + "!" );
					}
				} else {
					MainScreen.displayToConsole(p.getName() + " can't draw because the deck is empty!");
				}
				MainScreen.displayToConsole(p.getName() + "'s hand: " + p.getHand());
				continue;
			}
			else if (turnValue == 1) {
				MainScreen.displayToConsole(p.getName() + " played this turn!");
				if(p.getHandCount() == 0) {
					MainScreen.displayToConsole(p.getName() + " says: 'RUMMIKUB!' They won the game!");
					MainScreen.disableButtons();
					break;
				}
				MainScreen.displayToConsole(p.getName() + "'s hand: " + p.getHand());
				continue;
			}
		}		
		
		MainScreen.updateDisplayHand(user.getTiles());
		MainScreen.updateDisplayTable(game.getTable());
		originator.setState(game.getTable(), user.getTiles());
		caretaker.add(user.getName(), originator.saveMemento());
	}
	
	public void undoTurn() {
		//TODO: We will need to consider setting a "current player" per turn.
		originator.restoreMemento(caretaker.get(user.getName()));
		
		game.setTable(originator.getState().getTable());
		user.setTiles(originator.getState().getHand());
		
		MainScreen.currentTurnMelds = new ArrayList<ArrayList<Tile>>();
		MainScreen.currentTurnUserUsedTiles = new ArrayList<Tile>();
		
		MainScreen.updateDisplayHand(user.getTiles());
		MainScreen.updateDisplayTable(game.getTable());
	}
	
	public void endTurn(ArrayList<Tile> currentTurnUserUsedTiles, List<ArrayList<Tile>> newTable) {
		// If the user didn't play anything this turn.
		if ((MainScreen.currentTurnMelds.isEmpty()) && MainScreen.currentTurnUserUsedTiles.isEmpty()) {
			if(!emptyDeck) {
				Tile draw = deck.dealTile();
				if(draw == null) {
					emptyDeck = true;
					MainScreen.displayToConsole(user.getName() + " tried drawing, but the deck was empty!");
				} else {
					MainScreen.displayToConsole("User draws " + draw.toString() + "!");
					user.addTile(draw);						
				}
			} else {
				MainScreen.displayToConsole(user.getName() + " tried drawing, but the deck was empty!");
			}
		}
		else {
			// If the user has not played their initial 30 meld yet
			if (!user.getInit30Flag()){
				// No table tiles allowed to be played until the initial 30 meld(s)
				int tileCount = 0;
				for (ArrayList<Tile> meld : MainScreen.currentTurnMelds) {
					for (Tile t : meld) {
						tileCount++;
					}
				}
				// If the tile count of hand melds is the same as the count of total tiles used, it means that only hand melds were played
				if (tileCount == MainScreen.currentTurnUserUsedTiles.size()) {
					// User has to play HAND melds that add up to 30 or more and nothing else
					int handMeldSum = 0;
					for (ArrayList<Tile> meld : MainScreen.currentTurnMelds) {
						handMeldSum += Meld.getValue(meld);
					}
					if (handMeldSum < 30) {
						MainScreen.displayToConsole("The value of the hand melds played do not add up to at least 30 points!");
						return;
					}
					else {
						user.playedInit30();
					}
				}
				else {
					MainScreen.displayToConsole("You must play the the initial greater than 30 valued hand meld(s) first!");
					return;
				}
			}
		}
		
		//remove the user used tiles from user hand
		for (int i = 0; i < currentTurnUserUsedTiles.size(); ++i) {
			user.removeTile(currentTurnUserUsedTiles.get(i));
		}
		game.setTable(newTable);
		
		MainScreen.currentTurnMelds.clear();
		MainScreen.currentTurnUserUsedTiles.clear();
		if(user.getHandCount() == 0) {
			MainScreen.displayToConsole(user.getName() + " says: 'RUMMIKUB!' They won the game!");
			MainScreen.disableButtons();
		} else {
			gameLoop();			
		}
	}
}
