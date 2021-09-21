package com.letgro.backend.Database;

import com.letgro.backend.Model.History;
import com.letgro.backend.Model.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author: Leyuan Loh
 */
@Repository
public interface HistoryDatabase extends JpaRepository<History,Integer> {
    History findOneById(Integer id);
    List<History> findHisByAccountId(Integer id);
}