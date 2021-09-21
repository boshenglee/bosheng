package com.letgro.backend.Service;

import com.letgro.backend.Database.HistoryDatabase;
import com.letgro.backend.Model.History;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;
import java.util.stream.Collectors;

public class HistoryService {

    public static float totalSpendingAll(HistoryDatabase hd){
        float sumAll = (float) 0;
        List<History> allHis = hd.findAll();
        for(int i = 0; i < allHis.size() ; i++){
            sumAll = sumAll + allHis.get(i).getPrice();
        }
        return sumAll;
    }

    public static String mostBuyStoreAll(HistoryDatabase hd){
        List<History> allHis = hd.findAll();
        Map<History, Integer> storeCount = new HashMap<>();
        for(History s: allHis)
        {
            Integer c = storeCount.get(s);
            if(c == null) c = 0;
            c++;
            storeCount.put(s,c);
        }
        Map.Entry<History,Integer> mostRepeated = null;
        for(Map.Entry<History, Integer> e: storeCount.entrySet())
        {
            if(mostRepeated == null || mostRepeated.getValue()<e.getValue())
                mostRepeated = e;
        }
        return mostRepeated.getKey().getStore();
    }

    public static float highestPurchaseAll(HistoryDatabase hd){
        List<History> allHis = hd.findAll();
        float max = Float.valueOf(0);
        for(int i =0; i<allHis.size();i++){
            if(max < allHis.get(i).getPrice()){
                max = allHis.get(i).getPrice();
            }
        }
        return max;
    }

    public static float totalSpending( HistoryDatabase hd, @PathVariable Integer id){
        Float total = Float.valueOf(0);
        List<History> allHis = hd.findAll();
        List<History> userHis = new ArrayList<>();
        for(int i = 0; i <= allHis.size()-1; i++){
            if(allHis.get(i).getId() == id){
                userHis.add(allHis.get(i));
            }
        }
        for(int i = 0; i < userHis.size() ; i++){
            total += userHis.get(i).getPrice();
        }
        return total;
    }

    public static String mostBuyStore(HistoryDatabase hd, @PathVariable Integer id){
        List<History> allHis = hd.findAll();
        List<History> userHis = new ArrayList<>();
        for(int i = 0; i <= allHis.size()-1; i++){
            if(allHis.get(i).getId() == id){
                userHis.add(allHis.get(i));
            }
        }
        Map<History, Integer> storeCount = new HashMap<>();
        for(History s: userHis)
        {
            Integer c = storeCount.get(s);
            if(c == null) c = 0;
            c++;
            storeCount.put(s,c);
        }
        Map.Entry<History,Integer> mostRepeated = null;
        for(Map.Entry<History, Integer> e: storeCount.entrySet())
        {
            if(mostRepeated == null || mostRepeated.getValue()<e.getValue())
                mostRepeated = e;
        }
        return mostRepeated.getKey().getStore();
    }

    public static float highestPurchase(HistoryDatabase hd, @PathVariable Integer id){
        List<History> allHis = hd.findAll();
        List<History> userHis = new ArrayList<>();
        for(int i = 0; i <= allHis.size()-1; i++){
            if(allHis.get(i).getId() == id){
                userHis.add(allHis.get(i));
            }
        }
        float max = '0';
        for(int i =0; i<userHis.size();i++){
            if(max < userHis.get(i).getPrice()){
                max = userHis.get(i).getPrice();
            }
        }
        return max;
    }

    public static Map<String, Double> totalOfDate(HistoryDatabase hd, @PathVariable Integer id){
        List<History> allHis = hd.findAll();
        List<History> userHis = new ArrayList<>();
        for(int i = 0; i < allHis.size(); i++){
            if(allHis.get(i).getId() == id){
                userHis.add(allHis.get(i));
            }
        }

        HashMap allDate = new HashMap();
        for(History i : userHis)
        {
            String date = i.getGroDate();
            Float price = i.getPrice();

            allDate.put(date, price);
        }

        Map<String, Double> map = allHis.stream()
                .collect(Collectors.groupingBy(History::getGroDate,
                        Collectors.summingDouble(History::getPrice)));
        return map;
    }

}

