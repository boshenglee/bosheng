package coms362.cards.slapjack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import coms362.cards.abstractcomp.Move;
import coms362.cards.abstractcomp.Player;
import coms362.cards.abstractcomp.Table;
import coms362.cards.app.ViewFacade;
import coms362.cards.events.remote.CreateButtonRemote;
import coms362.cards.events.remote.CreatePileRemote;
import coms362.cards.events.remote.SetBottomPlayerTextRemote;
import coms362.cards.events.remote.SetGameTitleRemote;
import coms362.cards.events.remote.SetupTable;
import coms362.cards.fiftytwo.DealButton;
import coms362.cards.model.Card;
import coms362.cards.model.Location;
import coms362.cards.model.Pile;

public class SJInitCmd implements Move {
    
    private Table table;
	private Map<Integer, Player> players;
	private String title;
	
	public SJInitCmd(Map<Integer, Player> players, String game, Table table) {
		this.players = players;
		this.title = game;
		this.table = table;
	}

	public void apply(Table table){
		Pile player1Pile = new Pile(SJRules.PLAYER1_PILE, new Location(310,80), false);
		Pile player2Pile = new Pile(SJRules.PLAYER2_PILE, new Location(310,520), false);
		Pile centerPile = new Pile(SJRules.CENTER_PILE, new Location(310,300), true);

        try {
            for (String suit : Card.suits) {
                for (int i = 1; i <= 13; i++) {
                    Card card = new Card();
                    card.setSuit(suit);
                    card.setRank(i);
                    card.setX(310);
                    card.setY(80);
                    card.setRotate(0);
                    card.setFaceUp(false);
                    centerPile.addCard(card);
                }
            }
            shuffle(centerPile);
            
            Boolean pileFlip = true;
            for (Card c : centerPile.getCards()) {
            	if (pileFlip) {
            		c.setY(80);
            		player1Pile.addCard(c);
            		pileFlip = false;
                    table.getPlayer(1).addToScore(1);
            	} else {
            		c.setY(520);
            		player2Pile.addCard(c);
            		pileFlip = true;
                    table.getPlayer(2).addToScore(1);
            	}
    		}
            centerPile.getCards().clear();
            
            table.addPile(player1Pile);
			table.addPile(player2Pile);
			table.addPile(centerPile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	private Pile shuffle(Pile pile) {
		List cards = new ArrayList(pile.getCards());
		Collections.shuffle(cards);
		
		pile.getCards().clear();
		
		for (Object c : cards) {
			Card card = new Card();
            card.setSuit(((Card) c).getSuit());
            card.setRank(((Card) c).getRank());
            card.setX(((Card) c).getX());
            card.setY(((Card) c).getY());
            card.setRotate(((Card) c).getRotate());
            card.setFaceUp(((Card) c).isFaceUp());
            pile.addCard(card);
		}
		
		return pile;
	}

	public void apply(ViewFacade view) {
		view.send(new SetupTable());
		view.send(new SetGameTitleRemote(title));

		for (Player p : players.values()){
			String role = (p.getPlayerNum() == 1) ? "Dealer" : "Player "+p.getPlayerNum();
			view.send(new SetBottomPlayerTextRemote(role, p));
		}

		view.send(new CreatePileRemote(table.getPile(SJRules.PLAYER1_PILE)));
		view.send(new CreatePileRemote(table.getPile(SJRules.PLAYER2_PILE)));
		view.send(new CreatePileRemote(table.getPile(SJRules.CENTER_PILE)));

		DealButton dealButton = new DealButton("DEAL", new Location(0, 0));
		view.register(dealButton); //so we can find it later. 
		view.send(new CreateButtonRemote(dealButton));
		//view.send(new CreateButtonRemote(Integer.toString(getNextId()), "reset", "RestartGame", "Reset", new Location(500,0)));
		//view.send(new CreateButtonRemote(Integer.toString(getNextId()), "clear", "ClearTable", "Clear Table", new Location(500,0)));
	}
	
}
