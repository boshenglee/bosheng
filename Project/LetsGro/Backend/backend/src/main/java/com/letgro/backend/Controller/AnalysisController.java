package com.letgro.backend.Controller;


import com.letgro.backend.Database.HistoryDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.letgro.backend.Service.HistoryService;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AnalysisController {

    @Autowired
    HistoryDatabase hd;

    /**
     * return the result from history service.
     * @return analysis
     */
    @RequestMapping("/analysis")
    String analysisHis() {
        String analysis = HistoryService.totalSpendingAll(hd) + "," + HistoryService.mostBuyStoreAll(hd) + "," + HistoryService.highestPurchaseAll(hd);
        return analysis;
    }

    /**
     * return the result from history service for one user.
     * @return
     */
    @RequestMapping("/analysis/account/{id}")
    String analysisHisforOne(@PathVariable Integer id) {
        String analysis = HistoryService.totalSpending(hd, id) + "," + HistoryService.mostBuyStore(hd, id) + "," + HistoryService.highestPurchase(hd, id);
        return analysis;
    }

    /**
     * return the total, and the date of the history of one account
     * @param id
     * @return
     */
    @RequestMapping("/analysisByDate/account/{id}")
    String totalInDate(@PathVariable Integer id){
        String result = "";
        for(Map.Entry<String, Double> entry: HistoryService.totalOfDate(hd,id).entrySet()){
            String key = entry.getKey();
            Double f = entry.getValue();
            result = result + key + " " + f + " ";
        }
        return result;
    }
}
