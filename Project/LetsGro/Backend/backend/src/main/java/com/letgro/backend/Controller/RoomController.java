package com.letgro.backend.Controller;

import com.letgro.backend.Database.LoginDatabase;
import com.letgro.backend.Database.RoomDatabase;
import com.letgro.backend.Database.ToBuyDatabase;
import com.letgro.backend.Model.Account;
import com.letgro.backend.Model.Room;
import com.letgro.backend.Model.ToBuy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Scanner;
import java.util.List;

@RestController
public class RoomController {
    @Autowired
    RoomDatabase rd;

    @Autowired
    LoginDatabase ld;

    @Autowired
    ToBuyDatabase td;

    //First element integer and second is string
    @PostMapping("/room")
    String createRoom(@RequestBody String string) {
        string = string.replace("{", "");
        string = string.replace("}", "");
        string = string.replace("\"", "");
        string = string.replace("data:", "");
        Scanner sc = new Scanner(string);
        Account account1 = ld.findOneById(sc.nextInt());
        Account account2 = ld.findOneByUser(sc.next());

        List<Room> rooms = rd.findAll();
        int roomCount = 1;

        for (Room room : rooms) {
            if (room.getId() != roomCount) {
                break;
            } else
                roomCount++;
        }

        if (account1 == null && account2 == null) {
            return "{\"Data\" : \"False\"}";
        }
        if (account1.getRoom() != null || account2.getRoom() != null) {
            return "{\"Data\" : \"False\"}";
        }
        Room room = new Room(roomCount);
        rd.save(room);
        account1.setRoom(room);
        ld.save(account1);
        account2.setRoom(room);
        ld.save(account2);
        return "{\"Data\" : \"" + room.getId() + "\n" + account2.getAccountId();
    }

    @RequestMapping("/rooms")
    List<Room> getRooms() {
        return rd.findAll();
    }

    @DeleteMapping("/deleteRoom/{id}")
    String deleteRoom(@PathVariable Long id) {
        Room temp = rd.findOneById(id);
        if (temp == null) {
            return "No such room.";
        }
        List<Account> accounts = temp.getAccounts();
        List<ToBuy> toBuys = temp.getToBuys();

        for (ToBuy toBuy : toBuys) {
            td.delete(toBuy);
        }

        for (Account acc : accounts) {
            acc.setRoom(null);
            ld.save(acc);
        }


        rd.delete(temp);
        return "Deleted room.";
    }

    @GetMapping("/roomCheck/{id}")
    String checkUser(@PathVariable Integer id) {
        Account temp = ld.findOneById(id);

        if (temp.getRoom() != null) {
            Room room = rd.findOneById(temp.getRoom().getId());
            String output = room.getId() + "\n";

            List<Account> rooms = room.getAccounts();
            for (Account account : rooms) {
                if (account.getAccountId() != id) {
                    output += account.getUser() + "\n";
                }
            }
            return output;
        }
        return "False";
    }
}
