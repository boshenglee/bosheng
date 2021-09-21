package com.letgro.backend.Controller;

import com.letgro.backend.Database.LoginDatabase;
import com.letgro.backend.Database.ReceiptDatabase;
import com.letgro.backend.Model.Account;
import com.letgro.backend.Model.Receipt;
import com.letgro.backend.Service.ReceiptInterpreter.Walmart;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for receipt.
 * This class is in charged of requests such as post, get and delete.
 * Author: Leyuan Loh
 */
@RestController
@ApiOperation(value = "Controller for receipt. This class is in charged of requests such as post, get and delete.")
public class ReceiptController {
    /**
     * Injecting receiptDatabase to get access the data.
     */
    @Autowired
    ReceiptDatabase db;

    /**
     * Injecting LoginDatabase to get access the data.
     */
    @Autowired
    LoginDatabase ld;

    /**
     * Walmart service
     */
    @Autowired
    Walmart walmart;


    /**
     * Get a single receipt using the id.
     *
     * @param id
     * @return
     */
    @GetMapping("/receipt/{id}")
    Receipt getReceipt(@PathVariable long id) {
        return db.findOneById(id);
    }

    /**
     * Get a list of to receipts.
     *
     * @return
     */
    @RequestMapping("/receipts")
    List<Receipt> getReceipts() {
        return db.findAll();
    }

    /**
     * Get at list of to receipts of an user.
     * @param id
     * @return
     */
    @RequestMapping("/receipts/account/{id}")
    List<Receipt> getReceiptsByAccountId(@PathVariable Integer id){
        return db.findByAccountId(id);
    }


    /**
     * Send a to receipt to the database.
     * @param p
     * @return
     */
    @PostMapping("/receipt")
    Receipt createReceipt(@RequestBody Receipt p) {
        int receiptId = 1;
        List<Receipt> receipts = db.findAll();
        for (Receipt receipt : receipts) {
            if (receipt.getId() != receiptId) {
                break;
            } else
                receiptId++;
        }
        Receipt temp = new Receipt(receiptId, p.getImgLocation(), p.getAccount());
        db.save(temp);
        return p;
    }

    /**
     * The walmart extractor will store the information in the ocrText into historyDatabase.
     * @param p
     * @return
     * @throws Exception
     */
    @PostMapping("/receipt/ocrText/account/{id}")
    String sendOcrText(@RequestBody String p, @PathVariable int id) throws Exception {
        try{
            return walmart.extract(p, id);
        }
        catch(Exception ex){
            throw new Exception("Please only send me receipt format");
        }
    }

    /**
     * Send receipt with the account id.
     * The method will find the account with the id.
     * Create a receipt object with the account object and save it into the receipt database.
     */
    @PostMapping("/receipt/account/{id}")
    Receipt createReceiptWithAccountId(@RequestBody Receipt p, @PathVariable Integer id){
        Account account = ld.findOneById(id);
        int receiptId = 1;
        List<Receipt> receipts = db.findAll();
        for (Receipt receipt : receipts) {
            if (receipt.getId() != receiptId) {
                break;
            } else
                receiptId++;
        }
        Receipt temp = new Receipt(receiptId, p.getImgLocation(), account);
        account.addReceipt(temp);
        db.save(temp);
        return temp;
    }

    /**
     * Delete receipt by id or -1 which deletes all.
     * @param id
     * @return
     */
    @DeleteMapping("/receipt/{id}")
    String deleteReceipt(@PathVariable long id) {
        Receipt delete = db.findOneById(id);
        if(delete!=null){
            db.delete(delete);
            return "deleted "+id;
        }
        return "Receipt not found.";
    }
}
