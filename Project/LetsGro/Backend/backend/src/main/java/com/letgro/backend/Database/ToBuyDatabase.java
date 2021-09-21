package com.letgro.backend.Database;


import com.letgro.backend.Model.Account;
import com.letgro.backend.Model.ToBuy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Database to store toBuy entity. The database is connected to LoginDatabase in many to one relationship.
 * Author: Leyuan Loh
 */
@Repository
public interface ToBuyDatabase extends JpaRepository<ToBuy, Long> {

    /**
     * This method search through the tobuyDatabase and return an tobuy object with the same id.
     * @param id
     * @return ToBuy
     */
    ToBuy findOneById(Long id);

    /**
     * This method search through the tobuyDatabase return list of tobuy objects with the same
     * account id.
     * @param id
     * @return List<ToBuy>
     */
    List<ToBuy> findByAccountId(Integer id);
}
