package coms362.cards.slapjack;

import coms362.cards.abstractcomp.Move;
import coms362.cards.abstractcomp.Player;
import coms362.cards.abstractcomp.Table;
import coms362.cards.events.inbound.CardEvent;
import coms362.cards.events.inbound.DealEvent;
import coms362.cards.events.inbound.InitGameEvent;
import coms362.cards.fiftytwo.*;
import coms362.cards.model.Card;
import coms362.cards.model.Pile;

public class SJRules extends P52Rules {
    
	public static final String PLAYER1_PILE = "player1_pile";
	public static final String PLAYER2_PILE = "player2_pile";
    public static final String CENTER_PILE = "center_pile";
    public static int playerTurn=1;
	public SJRules() {
		super();
	}

	@Override
	public Move apply(CardEvent e, Table table, Player player){
		int opponentPlayerID = (player.getPlayerNum()==1)?2:1;
		Pile centerPile = table.getPile(CENTER_PILE);
		if(centerPile.getCard(e.getId()) != null){ // Player clicked on center pile
			return clickOnCenterPile(table, player, centerPile, e, opponentPlayerID);
		}
		else if(playerTurn==player.getPlayerNum()){ // Player clicked on either pile 1 or 2
			return clickOnPlayerPile(table, player, centerPile, e, opponentPlayerID);
		}
		return new DropEventCmd();
	}

	private Move clickOnCenterPile(Table table, Player player, Pile fromPile, CardEvent e, int opponentPlayerID){
		Pile toPile = (player.getPlayerNum()==1) ? table.getPile(PLAYER1_PILE) : table.getPile(PLAYER2_PILE);
		Card c = fromPile.getCard(e.getId()); // Card from Center Pile
		if (c.getRank()==11) { // is Jack Card
			if(table.getPlayer(opponentPlayerID).getScore()==0) {
				return new SJEndPlayMove(table, player);
			}
			else {
				playerTurn = player.getPlayerNum();
				return new SJSlapJackMove(c, player, fromPile, toPile, CENTER_PILE, ((player.getPlayerNum()==1) ? PLAYER1_PILE : PLAYER2_PILE));
			}
		}
		else{ 	// Optional Feature: if user clicks on center pile and J is not on the top
			playerTurn = opponentPlayerID;
			toPile = (player.getPlayerNum()==1) ? table.getPile(PLAYER2_PILE) : table.getPile(PLAYER1_PILE);
			return new SJWrongMove(c, table.getPlayer(opponentPlayerID), fromPile, toPile, CENTER_PILE, ((player.getPlayerNum()==1) ? PLAYER2_PILE : PLAYER1_PILE));
		}
	}

	private Move clickOnPlayerPile(Table table, Player player, Pile toPile, CardEvent e, int opponentPlayerID){
		Pile fromPile = (player.getPlayerNum()==1) ? table.getPile(PLAYER1_PILE) : table.getPile(PLAYER2_PILE);
		Card c = fromPile.getCard(e.getId());  // Card from Player Pile
		if (c == null) {
			return new DropEventCmd();
		}
		playerTurn = opponentPlayerID;
		if(table.getPlayer(opponentPlayerID).getScore()==0) {
			playerTurn = player.getPlayerNum();
		}
		return new SJMove(c, player, fromPile, toPile, ((player.getPlayerNum()==1) ? PLAYER1_PILE : PLAYER2_PILE), CENTER_PILE);
	}

	@Override
	public Move apply(DealEvent e, Table table, Player player){
		return new SJDealCommand(table, player);
	}

	@Override
	public Move apply(InitGameEvent e, Table table, Player player){
		return new SJInitCmd(table.getPlayerMap(), "SlapJack", table);
	}

}
