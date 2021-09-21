package com.letgro.backend.Controller;

import java.util.List;
import com.letgro.backend.Database.LoginDatabase;
import com.letgro.backend.Model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Login controller
 * Author: Triet Than, Leyuan
 */

@RestController
public class LoginController {
    @Autowired
    LoginDatabase ld;

    /**
     * Get the account by its ID
     * @param id
     * @return
     */
    @GetMapping("/FindAccountById/{id}")
    Account getAccount(@PathVariable Integer id) {
        return ld.findOneById(id);
    }

    /**
     * Get the account by its user
     * @param user
     * @return
     */
    @GetMapping("/FindAccountByUser/{user}")
    Account getAcocunt(@PathVariable String user) {
        return ld.findOneByUser(user);
    }


    /**
     * check either the account already exists or not
     * @param a
     * @return
     */
    @PostMapping("/account/check")
    Account checkAccount(@RequestBody Account a){
        Account temp = ld.findOneByUser(a.getUser());
        if(temp!=null){
            if(temp.getPassword().equals(a.getPassword())){
               return temp;
            }
        }
        return new Account(0, null,null,null,null);
    }

    /**
     * return the list of all the account
     * @return
     */
    @RequestMapping("/accounts")
    List<Account> hello() {
        return ld.findAll();
    }

    /**
     * save a new account to the database
     * @param a
     * @return
     */
    @PostMapping("/account")
    Account createAccount(@RequestBody Account a) {
        List<Account> accounts = ld.findAll();
        int idCount =1;

        for(Account account: accounts){
            if(account.getAccountId()!= idCount){
                break;
            }else
                idCount++;
        }
        Account newAcc = new Account(idCount, a.getUser(), a.getEmail(), a.getPassword(),null);
        if (newAcc.getUser() != null && newAcc.getEmail() != null && newAcc.getPassword() != null) {
            if (ld.findByEmail(a.getEmail()).size() >= 1 || ld.findByUser(a.getUser()).size() >= 1) {
                Account notExist = new Account(0,null,null,null,null);
                return notExist;
            }
            ld.save(newAcc);
            return newAcc;
        }
        return new Account(0, null,null,null,null);
    }


    /**
     * modify an account in the database
     * @param a
     * @param id
     * @return
     */
    @PutMapping("/account/{id}")
    Account updatePerson(@RequestBody Account a, @PathVariable Integer id) {
        Account old_a = ld.findOneById(id);
        old_a.setEmail(a.getEmail());
        old_a.setUser(a.getUser());
        old_a.setPassword(a.getPassword());

        ld.save(old_a);
        return old_a;
    }

    /**
     * Delete a acount by its id.
     * @param id
     * @return
     */
    @DeleteMapping("/account/{id}")
    String deleteAccount(@PathVariable Integer id) {
        Account delete = ld.findOneById(id);
        ld.delete(delete);
        return "deleted " + id;
    }

}
