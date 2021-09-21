package com.letgro.backend.Socket;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import java.util.List;
import java.util.Scanner;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.letgro.backend.Database.LoginDatabase;
import com.letgro.backend.Database.RoomDatabase;
import com.letgro.backend.Database.ToBuyDatabase;
import com.letgro.backend.Model.Account;
import com.letgro.backend.Model.Room;
import com.letgro.backend.Model.ToBuy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller // this is needed for this to be an endpoint to springboot
@ServerEndpoint(value = "/room/{id}")  // this is Websocket url
public class RoomSocket {

    private static RoomDatabase rd;

    private static LoginDatabase ld;

    private static ToBuyDatabase td;

    @Autowired
    public void setLoginDatabase(LoginDatabase ld) {
        this.ld = ld;
    }

    @Autowired
    public void setRoomDatabase(RoomDatabase rd) {
        this.rd = rd;
    }

    @Autowired
    public void setToBuyDatase(ToBuyDatabase td) {
        this.td = td;
    }

    private static Map<Session, Integer> sessionUserIdMap = new Hashtable<>();
    private static Map<Integer, Session> userIdSessionMap = new Hashtable<>();

    private final Logger logger = LoggerFactory.getLogger(RoomSocket.class);

    @OnOpen
    public void onOpen(Session session, @PathParam("id") String id) {
        logger.info("Entered into Open");

        try {
            Integer intId = Integer.parseInt(id);
            sessionUserIdMap.put(session, intId);
            userIdSessionMap.put(intId, session);

            sendMessageToParticularUser(intId, getTobuyList(intId));
        } catch (NumberFormatException e) {
            logger.info("Exception: " + e.getMessage().toString());
        }

    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        logger.info("Entered into Message: Got Message:" + message);
        Integer userId = sessionUserIdMap.get(session);

        Room room = ld.findOneById(userId).getRoom();
        List<Account> accounts = room.getAccounts();

        Scanner sc = new Scanner(message);
        if (sc.next().equals("D")) {
            deleteToBuyItem(room, message);
            return;
        }

        sc.close();


        List<ToBuy> toBuys = td.findAll();
        int itemId = 1;
        for (ToBuy toBuy : toBuys) {
            if (toBuy.getId() != itemId) {
                break;
            } else
                itemId++;
        }


        ToBuy toBuy = new ToBuy(itemId, message, null, room);
        td.save(toBuy);

        for (Account account : accounts) {
            if (userIdSessionMap.containsKey(account.getAccountId())) {
                sendMessageToParticularUser(account.getAccountId(), toBuy.toString() + " \n");
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        logger.info("Entered into Error");
        throwable.printStackTrace();
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        logger.info("Entered into Close");

        Integer userId = sessionUserIdMap.get(session);
        sessionUserIdMap.remove(session);
        userIdSessionMap.remove(userId);
    }

    private void sendMessageToParticularUser(int id, String item) {
        try {

            userIdSessionMap.get(id).getBasicRemote().sendText(item);
        } catch (IOException e) {
            logger.info("Exception: " + e.getMessage().toString());
            e.printStackTrace();
        }
    }

    private void deleteToBuyItem(Room room, String item) {
        Scanner sc = new Scanner(item);
        sc.next();
        List<Account> accounts = room.getAccounts();
        ToBuy delete = td.findOneById(Long.parseLong(sc.next()));
        if (delete != null) {
            td.delete(delete);
            for(Account account : accounts){
                if(userIdSessionMap.containsKey(account.getAccountId())){
                    try {
                        userIdSessionMap.get(account.getAccountId()).getBasicRemote().sendText(item);
                    } catch (IOException e) {
                        logger.info("Exception: " + e.getMessage().toString());
                        e.printStackTrace();
                    }
                }
            }
        }
        sc.close();
    }


    private String getTobuyList(int userId) {
        Account account = ld.findOneById(userId);
        Room room = rd.findOneById(account.getRoom().getId());

        List<ToBuy> toBuys = room.getToBuys();
        StringBuilder sb = new StringBuilder();
        for (ToBuy toBuy : toBuys) {
            sb.append(toBuy + "\n");
        }

        return sb.toString();
    }
}
