package coms362.cards.slapjack;

import coms362.cards.abstractcomp.Player;
import coms362.cards.abstractcomp.Table;
import coms362.cards.app.ViewFacade;
import coms362.cards.events.remote.*;
import coms362.cards.model.Card;
import coms362.cards.model.Pile;

import java.util.ArrayList;

public class SJSlapJackMove extends SJMove{

    protected ArrayList<Card> cardList;

    public SJSlapJackMove(Card c, Player p, Pile fromPile, Pile toPile, String fromPileString, String toPileString) {
        super(c, p, fromPile, toPile, fromPileString, toPileString);
    }

    @Override
    public void apply(Table table) {
        cardList = new ArrayList<>();
        fromPile.getCards().iterator().forEachRemaining(card -> cardList.add(card));
        for(Card card: cardList){
            table.removeFromPile(fromPileString, card);
            table.addToPile(toPileString,card);
            table.addToScore(p, 1);
        }
    }

    @Override
    public void apply(ViewFacade view) {
        for(Card card: cardList){
            view.send(new HideCardRemote(card));
            view.send(new RemoveFromPileRemote(fromPile, card));
            view.send(new AddToPileBottomRemote(toPile, card));
        }
        view.send(new UpdatePileRemote(toPile));
        view.send(new ShowPlayerScore(p, null));
    }
}