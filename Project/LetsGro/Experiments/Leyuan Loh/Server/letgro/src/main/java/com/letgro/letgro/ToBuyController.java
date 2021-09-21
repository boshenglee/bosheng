package com.letgro.letgro;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ToBuyController {

    @Autowired
    ToBuyDatabase db;

    @GetMapping("/toBuyItem/{id}")
    ToBuy getItem(@PathVariable Integer id) {
        return db.findOne(id);
    }

    @RequestMapping("/toBuyItems")
    List<ToBuy> hello(){
        return db.findAll();
    }

    @PostMapping("/toBuyItem")
    ToBuy createToBuy(@RequestBody ToBuy p){
        db.save(p);
        return p;
    }

    @DeleteMapping("/toBuyItem/{id}")
    String deletePerson(@PathVariable Integer id){
        db.delete(id);
        return "deleted " + id;
    }
}
