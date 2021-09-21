package com.letgro.backend.Database;

import com.letgro.backend.Model.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

/**
 * Database to store receipt entity. The database is connected to LoginDatabase in many to one relationship.
 * Author: Leyuan Loh
 */
@Repository
public interface ReceiptDatabase extends JpaRepository<Receipt, Long> {

    /**
     * This method search through the receiptDatabase and return a receipt object with the same id.
     * @param id
     * @return Receipt
     */
    Receipt findOneById(Long id);

    /**
     * This method search through the receiptDatabase and return a list of receipt objects with the same account id.
     * @param id
     * @return List<Receipt>
     */
    List<Receipt> findByAccountId(Integer id);
}
