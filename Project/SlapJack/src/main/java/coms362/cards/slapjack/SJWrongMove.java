package coms362.cards.slapjack;

import coms362.cards.abstractcomp.Player;
import coms362.cards.model.Card;
import coms362.cards.model.Pile;

public class SJWrongMove extends SJSlapJackMove{
    public SJWrongMove(Card c, Player p, Pile fromPile, Pile toPile, String fromPileString, String toPileString) {
        super(c, p, fromPile, toPile, fromPileString, toPileString);
    }
}
