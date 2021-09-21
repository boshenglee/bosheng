package coms362.cards.slapjack;

import java.util.Map;

import coms362.cards.abstractcomp.Move;
import coms362.cards.abstractcomp.Player;
import coms362.cards.abstractcomp.Table;
import coms362.cards.app.ViewFacade;
import coms362.cards.events.remote.SetGameTitleRemote;


public class SJEndPlayMove implements Move {
	
	private Player p;
	private Table table;
		
	public SJEndPlayMove(Table table, Player p) {
		this.p = p;
		this.table = table;
	}

	public void apply(Table table) {
		table.setMatchOver(true);
		// TODO Auto-generated method stub

	}

	public void apply(ViewFacade view) {	
		// TODO Auto-generated method stub
		String winTitle = "Player " + p.getPlayerNum() + " Wins!";
		view.send(new SetGameTitleRemote(winTitle));
	}
	
	@Override	
	public boolean isMatchEnd(){
		return true;
	}

}
