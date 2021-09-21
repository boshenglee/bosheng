package coms362.cards.slapjack;

import coms362.cards.abstractcomp.Move;
import coms362.cards.abstractcomp.Player;
import coms362.cards.abstractcomp.Table;
import coms362.cards.app.ViewFacade;
import coms362.cards.events.remote.AddToPileRemote;
import coms362.cards.events.remote.CreateCardRemote;
import coms362.cards.events.remote.HideButtonRemote;
import coms362.cards.events.remote.UpdateCardRemote;
import coms362.cards.fiftytwo.DealButton;
import coms362.cards.model.Card;
import coms362.cards.model.Pile;

public class SJDealCommand implements Move {
    private Table table;

    public SJDealCommand(Table table, Player player) {
        this.table = table;
    }

    public void apply(Table table) {
        // TODO Auto-generated method stub

    }

    public void apply(ViewFacade views) {

        try {
            String remoteId = views.getRemoteId(DealButton.kSelector);
            views.send(new HideButtonRemote(remoteId));
            Pile player1 = table.getPile(SJRules.PLAYER1_PILE);
            Pile player2 = table.getPile(SJRules.PLAYER2_PILE);
            if (player1 == null || player2 == null) {
                return;
            }
            for (Card c : player1.getCards()) {
                String outVal = "";
                views.send(new CreateCardRemote(c));
                views.send(new UpdateCardRemote(c));
                views.send(new AddToPileRemote(player1,c)); //new things added
                System.out.println(outVal);
            }
            for (Card c : player2.getCards()) {
                String outVal = "";
                views.send(new CreateCardRemote(c));
                views.send(new UpdateCardRemote(c));
                views.send(new AddToPileRemote(player2,c)); //new things added
                System.out.println(outVal);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

