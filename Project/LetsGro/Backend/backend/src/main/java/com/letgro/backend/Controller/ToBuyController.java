package com.letgro.backend.Controller;


import java.util.List;


import com.letgro.backend.Database.LoginDatabase;
import com.letgro.backend.Database.RoomDatabase;
import com.letgro.backend.Model.Account;
import com.letgro.backend.Model.Room;
import com.letgro.backend.Model.ToBuy;
import com.letgro.backend.Database.ToBuyDatabase;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Controller for to buy.
 * This class is in charged of requests such as post, get and delete.
 * Author: Leyuan Loh
 */
@RestController
@ApiOperation(value = "Controller for to buy. This class is in charged of requests such as post, get and delete.")
public class ToBuyController {

    /**
     * Injecting toBuyDatabase to get access the data.
     */
    @Autowired
    ToBuyDatabase db;

    /**
     * Injecting LoginDatabase to get access the data.
     */
    @Autowired
    LoginDatabase ld;


    /**
     * Get a single to buy item using id.
     *
     * @param id
     * @return
     */
    @GetMapping("/toBuyItem/{id}")
    ToBuy getItem(@PathVariable long id) {
        return db.findOneById(id);
    }


    /**
     * Get a list of to buy items.
     *
     * @return
     */
    @RequestMapping("/toBuyItems")
    List<ToBuy> getItems() {
        return db.findAll();
    }

    /**
     * Get a lit of to buy items with the account id.
     *
     * @param id
     * @return
     */
    @RequestMapping("/toBuyItems/account/{id}")
    List<ToBuy> getItemsWithAccountId(@PathVariable Integer id) {
        return db.findByAccountId(id);
    }

    /**
     * Send array of to buy item to the database.
     *
     * @param p
     * @return
     */
    @PostMapping("/toBuyItemArray")
    List<ToBuy> createToBuyArray(@RequestBody List<ToBuy> p) {
        int i = 0;
        while (i < p.size()) {
            ToBuy temp = new ToBuy(db.count() + 1, p.get(i).getItem(), p.get(i).getAccount(), p.get(i).getRoom());
            db.save(temp);
            i++;
        }
        return p;
    }

    /**
     * Send a to buy item to the database.
     *
     * @param p
     * @return
     */
    @PostMapping("/toBuyItem")
    ToBuy createToBuy(@RequestBody ToBuy p) {
        int itemId = 1;
        List<ToBuy> toBuys = db.findAll();
        for (ToBuy toBuy : toBuys) {
            if (toBuy.getId() != itemId) {
                break;
            } else
                itemId++;
        }
        ToBuy temp = new ToBuy(itemId, p.getItem(), p.getAccount(), p.getRoom());
        db.save(temp);
        return temp;
    }

    /**
     * Send a to buy item with id of the account to the database.
     * The method will find the account with the id. Create the ToBuy object with the id, item, and account.
     * Save it into the tobuyDatabase.
     */
    @PostMapping("/toBuyItem/account/{id}")
    ToBuy createToBuyWithAccountId(@RequestBody ToBuy p, @PathVariable Integer id) {
        Account account = ld.findOneById(id);
        List<ToBuy> toBuys = db.findAll();
        int itemId = 1;
        for (ToBuy toBuy : toBuys) {
            if (toBuy.getId() != itemId) {
                break;
            } else
                itemId++;
        }
        ToBuy temp = new ToBuy(itemId, p.getItem(), account, p.getRoom());
        db.save(temp);
        return temp;
    }

    /**
     * Delete item by id or -1 which deletes all.
     *
     * @param id
     * @return
     */
    @DeleteMapping("/toBuyItem/{id}")
    String deleteToBuy(@PathVariable long id) {
        ToBuy delete = db.findOneById(id);
        if (delete != null) {
            db.delete(delete);
            return "deleted " + id;
        } else
            return "id is not in database";
    }

    @DeleteMapping("/toBuyItem/account/{id}")
    String deleteUserToBuy(@PathVariable long id) {
        Account account = ld.findOneById((int) id);
        List<ToBuy> temp = account.getToBuyList();
        for (ToBuy toBuy : temp) {
            ToBuy delete = db.findOneById(toBuy.getId());
            db.delete(delete);
        }
        return "Deleted";
    }
}

