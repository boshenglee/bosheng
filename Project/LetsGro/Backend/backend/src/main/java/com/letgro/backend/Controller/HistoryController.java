package com.letgro.backend.Controller;

import com.letgro.backend.Database.HistoryDatabase;
import com.letgro.backend.Database.LoginDatabase;
import com.letgro.backend.Model.Account;
import com.letgro.backend.Model.History;
import com.letgro.backend.Model.ToBuy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class HistoryController {
    @Autowired
    HistoryDatabase hd;

    @Autowired
    LoginDatabase ld;

    /**
     * Get the history's date by the id of the history
     *
     * @param id
     * @return
     */
    @GetMapping("/historyByDate/{id}")
    String getDate(@PathVariable Integer id) {
        return hd.findOneById(id).getGroDate();
    }

    /**
     * Get the history's date by the id of the history for the same account
     *
     * @param id
     * @return
     */
    @GetMapping("/historyByDate/account/{id}")
    String getDatebyId(@PathVariable Integer id) {
        return hd.findOneById(id).getGroDate();
    }

    /**
     * get the history by id
     *
     * @param id
     * @return
     */
    @GetMapping("/history/{id}")
    History getHis(@PathVariable Integer id) {
        return hd.findOneById(id);
    }

    /**
     * get the history by id of the same user
     *
     * @param id
     * @return
     */
    @GetMapping("/history/account/{id}")
    List<History> getHisById(@PathVariable Integer id) {
        return  hd.findHisByAccountId(id);
    }

    /**
     * all the history of a account
     *
     * @return
     */
    @RequestMapping("/histories/account/{id}")
    List<History> allHisByUser(@PathVariable Integer id) {
        Account account = ld.findOneById(id);
        return account.getHistories();
    }

    /**
     * save a new history into the database with user
     *
     * @param history
     * @return
     */
    @PostMapping("/history/account/{id}")
    History createHistory(@RequestBody History history, @PathVariable Integer id) {
        Account account = ld.findOneById(id);
        History newAcc = new History((int) hd.count() + 1, history.getItem(), history.getPrice(), history.getStore(), history.getGroDate(), account);
        if (newAcc.getItem() != null && newAcc.getStore() != null && newAcc.getGroDate() != null) {
            history.setId((int) hd.count() + 1);
            hd.save(newAcc);
            return history;
        }
        return null;
    }

    /**
     * save a new history into the database
     *
     * @param history
     * @return
     */
    @PostMapping("/history")
    History createHistoryWithUser(@RequestBody History history) {
        History newAcc = new History((int) hd.count() + 1, history.getItem(), history.getPrice(), history.getStore(), history.getGroDate(), history.getAccount());
        if (newAcc.getItem() != null && newAcc.getStore() != null && newAcc.getGroDate() != null) {
            history.setId((int) hd.count() + 1);
            hd.save(newAcc);
            return history;
        }
        return null;
    }

    /**
     * Modify an exist account
     *
     * @param a
     * @param id
     * @return
     */
    @PutMapping("/history/{id}")
    History updatePerson(@RequestBody History a, @PathVariable Integer id) {
        History old_a = hd.findOneById(id);
        old_a.setItem(a.getItem());
        old_a.setPrice(a.getPrice());
        old_a.setStore(a.getStore());
        old_a.setGroDate(a.getGroDate());
        hd.save(old_a);
        return old_a;
    }

    /**
     * delete a history by id
     *
     * @param id
     * @return
     */
    @DeleteMapping("/history/{id}")
    String deleteHistory(@PathVariable Integer id) {
        if (id == -1) {
            hd.deleteAll();
            return "deleted all";
        }
        History delete = hd.findOneById(id);
        hd.delete(delete);
        List<History> temp = hd.findAll();
        hd.deleteAll();
        int i = 0;
        while (i < temp.size()) {
            hd.save(new History(i + 1, temp.get(i).getItem(), temp.get(i).getPrice(), temp.get(i).getStore(), temp.get(i).getGroDate(), temp.get(i).getAccount()));
            i++;
        }
        return "deleted " + id;
    }
}
