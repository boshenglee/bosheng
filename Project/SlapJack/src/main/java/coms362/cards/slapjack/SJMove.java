package coms362.cards.slapjack;

import coms362.cards.abstractcomp.Move;
import coms362.cards.abstractcomp.Player;
import coms362.cards.abstractcomp.Table;
import coms362.cards.app.ViewFacade;
import coms362.cards.events.remote.*;
import coms362.cards.fiftytwo.P52Move;
import coms362.cards.fiftytwo.P52Player;
import coms362.cards.model.Card;
import coms362.cards.model.Location;
import coms362.cards.model.Pile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class SJMove implements Move{
	
	protected Card c;
	protected Player p;
	protected Pile fromPile;
	protected Pile toPile;
	protected String fromPileString;
	protected String toPileString;

	public SJMove(Card c, Player p, Pile fromPile, Pile toPile, String fromPileString, String toPileString){
		this.c = c;
		this.p = p;
		this.fromPileString = fromPileString;
		this.toPileString = toPileString;
		this.fromPile = fromPile;
		this.toPile = toPile;
	}

	public void apply(Table table) {
		table.removeFromPile(fromPileString, c);
		table.addToPile(toPileString, c);
		table.addToScore(p, -1);
	}

	public void apply(ViewFacade view) {
		view.send(new HideCardRemote(c));
		view.send(new RemoveFromPileRemote(fromPile, c));
		view.send(new AddToPileRemote(toPile, c));
		view.send(new ShowCardRemote(c));
		view.send(new ShowPlayerScore(p, null));
	}

}
