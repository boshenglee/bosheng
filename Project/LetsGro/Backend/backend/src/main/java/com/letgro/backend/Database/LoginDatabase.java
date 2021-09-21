package com.letgro.backend.Database;

import com.letgro.backend.Model.Account;
import com.letgro.backend.Model.ToBuy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author: Triet Than
 */

@Repository
public interface LoginDatabase extends JpaRepository<Account,Integer>{
    Account findOneByUser(String user);
    List<Account> findByEmail(String email);
    List<Account> findByUser(String user);
    Account findOneById(Integer id);
}
