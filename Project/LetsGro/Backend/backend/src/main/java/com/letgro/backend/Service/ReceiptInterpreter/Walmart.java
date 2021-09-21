package com.letgro.backend.Service.ReceiptInterpreter;

import com.letgro.backend.Database.HistoryDatabase;
import com.letgro.backend.Database.LoginDatabase;
import com.letgro.backend.Model.Account;
import com.letgro.backend.Model.History;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Walmart service helps to extract data from only Walmart receipt.
 * Author: Leyuan Loh
 */
@Service
public class Walmart {

    /**
     * Injecting HistoryDatabase so that my algorithm can save the historyItem
     * into the database.
     */
    @Autowired
    HistoryDatabase db;

    @Autowired
    LoginDatabase ld;

//    String text;
//
//    //    public Walmart(String orcText) {
////        this.ocrText = orcText;
////    }
////
//    public Walmart() {
//        this.text = "{\"data\":\"Give us feedback@survery.walmart.com\\nThank you! ID #: 7P9TCP1H2SXR\\nWalmart\\n515-956-3536 Mgr: JONATHAN\\n534 S DUFF AVE\\nAMES IA 50010\\nST# 04256 OP# 004927 TE# 04 TR# O5503\\nCANTALOUPE 000000004050KF\\nAT\\n1 FOR\\n1.93\\n3.86\\nN\\nGV WHT VNGR\\n007874235255 F\\n2.64 0\\n80 CHK GB\\n007874226969 F\\n14.86 0\\nWHOLE MILK\\n007874235186 F\\n2.34 0\\nEGGS 18CT\\n007874212708 F\\n1.57 0\\nDRUMSTICK\\n007562055872 F\\n4.86 0\\nSUBTOTAL\\n30.13\\nTOTAL\\n.13\\nDISCV TEND\\n30.13\\nDiscover Credit\\n*** **** ********| 1\\nAPPROVAL # 00521R\\nREF # 025000346657\\nAID A000001523010\\nAAC EA2942C1C07FFF16\\nTERMINAL # SCO10388\\n09\\/05\\/20\\n19:19:46\\nCHANGE DUE\\n0.00\\n# ITEMS SOLD\\nTCM 6568 8942 8290 9627 4498\\nLow Prices You Can Trust. Every Day.\\n09\\/05\\/20\\n19:19:46\\n***CUSTOMER COPY***\\n\"}";
////        this.ocrText = "{\"data\":\"Give us feedback@survery.walmart.com\\nThank you! ID #: 7P9TCP1H2SXR\\nWalmart\\n515-956-3536 Mgr: JONATHAN\\n534 S DUFF AVE\\nAMES IA 50010\\nST# 04256 OP# 004927 TE# 04 TR# O5503\\nCANTALOUPE 000000004050KF\\nAT\\n1 FOR\\n1.93\\n3.86\\nN\\nGV WHT VNGR\\n007874235255 F\\n2.64 0\\n80 CHK GB\\n007874226969 F\\n14.86 0\\nWHOLE MILK\\n007874235186 F\\n2.34 0\\nEGGS 18CT\\n007874212708 F\\n1.57 0\\nDRUMSTICK\\n007562055872 F\\n4.86 0\\nSUBTOTAL\\n30.13\\nTOTAL\\n30.13\\nDISCV TEND\\n30.13\\nDiscover Credit\\n*** ** ********| 1\\nAPPROVAL # 00521R\\nREF # 025000346657\\nAID A000001523010\\nAAC EA2942C1C07FFF16\\nTERMINAL # SCO10388\\n09\\/05\\/20\\n19:19:46\\nCHANGE DUE\\n0.00\\n# ITEMS SOLD\\nTCM 6568 8942 8290 9627 4498\\nLow Prices You Can Trust. Every Day.\\n09\\/05\\/20\\n19:19:46\\n***CUSTOMER COPY***\\n\"}";
////          this.ocrText="{\"data\":\"Give us feedback @ survey.walmart.com\\nThank you! ID #:7P9HSW1H20L\\nWalmart\\n515-956-3536 Mgr:\\/ONATHAN\\n534 S DUFF AVE\\nAMES IA 50010\\nST# 04256 OP# 004917 TE# 03 TR# 01747\\n80\\/20 GRD-BF 007874226960 F\\n7.90 0\\nDRUMSTICK 007562055872 F\\n4.86 0\\nGV TH HML 007874205302 F\\n15.22 0\\nCAN OPENER 007675333075\\n2.34 X\\nBOX GARLIC 007\\n690 00336 F\\n1.28 N\\nBANANAS\\n000000004011KF\\n2.02 lb@ 1 lb \\/0.47\\n0.95 N\\n3 CLEM\\n068113130530 F\\n4.34 N\\nVIT D MILK\\n007104310440 F\\n4.62 0\\nBLACK PEPPER 007874225238 F\\n4.98 0\\nWHOLE CORN 003710003662 F\\n0.76 0\\nWHOLE CORN 003710003662 F\\n.76 0\\nPEAS CARROT 002400016311 F\\n1.48 0\\nPEAS CARROT 002400016311 F\\n1.48 0\\nMRTON PL SAL 002460001001 F\\n0.92 0\\nJUMP ROPE 004288742843\\n3.98 X\\nBE SF BRCAUL 001450002132 F\\n1.97 0\\nRED UNION 000000004082KF\\n0.84 Ib 1 lb \\/0.98\\n0.82 N\\nSUBTOTAL\\n58.7 74\\nTAX 1 7.000%\\n0.4 14\\nTOTAL\\n59.18\\nDISCV TEND\\n59.18\\nDiscover Credit * ** ** ***1 | 1\\nAPPROVAL # 01363R\\nREF # 022600795297\\nAID A0000001523010\\nAAC C937517363F46D18\\nTERMINAL # SC010053\\n08\\/13\\/20 15:29:35\\nCHANGE DUE\\n0.00\\n#ITEMS SOLD 17\\nTC# 0247 7670 7952 5149 8637\\nLow Prices You Can Trust. Every Day.\\n08\\/13\\/20\\n15:29:36\\n***CUSTOMER COPY***\\n\"}";
////          this.ocrText="{\"data\":\"Give us feedback @ survey.walmart.com\\nThank you! ID #:7P9K2G1H2063\\nWalmart\\n515-956-3536 Mgr:JONATHAN\\n534 S DUFF AVE\\nAMES IA 50010\\nST# 04256 OP# 009030 TE# 30 TR# 02879\\nBEGBUG FLEA 007112196548\\n6.67\\nX\\nBESF FS CORN 001450001407 F\\n1.97 0\\nBE BROC\\/CR\\/S 001450002130 F\\n1.97 0\\nBE BROC\\/CR\\/S 001450002130 F\\n1.97 0\\nSUBTOTAL\\n12.58\\nTAX 1 7.000%\\n0.47\\nTOTAL\\n13.05\\nDISCV TEND\\n13.05\\nDiscover Credit * ** *******1 1 1\\nAPPROVAL # 02523R\\nREF #02380053829\\nAID A0O00001523010\\nAAC OAC9F437BOE998CA\\nTERMINAL# SC011389\\n08\\/25\\/20 08:24:26\\nCHANGE DUE\\n0.00\\n# ITEMS SOLD 4\\nTC# 0247 7670 7952 5149 8637\\nLow Prices You Can Trust. Every Day.\\n08\\/25\\/20 08:24:26\\n***CUSTOMER COPY***\\n\"}";
//    }
    
    /**
     * Extract helps get information such as location, item names, item prices, and date of the purchase.
     * The ocrText need to follow the receipt format else the algorithm will not work. My algorithm is dependant to the
     * format of the receipt. If there is one less line in the ocrText, my algorithm may not work correctly. My algorithm
     * works by scanning line by line and words by words.
     * @param ocrText
     * @return
     * @throws Exception
     */
    public String extract(String ocrText, int id) throws Exception {
        try {
            String temp = "";
            String location = "";
            ArrayList<String> itemName = new ArrayList<>();
            ArrayList<Float> itemPrice = new ArrayList<>();
            double price = 0;
            String date;
            ocrText = ocrText.replace("{\"data\":\"", "");
            ocrText = ocrText.replace("\"}", "");
            ocrText = ocrText.replace("\\n", "     newLine    ");
            ocrText = ocrText.replace("\\", "");
            Scanner input = new Scanner(ocrText);
            //Skip until the address
            for (int i = 0; i <= 3; i++) {
                while (!input.next().equals("newLine")) {
                }
            }
            //Extracting the address
            temp = input.next();
            while (!temp.equals("ST#")) {
                while (!temp.equals("newLine")) {
                    if (!temp.equals("newLine")) {
                        location += temp + " ";
                    }
                    temp = input.next();
                }
                temp = input.next();
            }
            //skip a line
            while (!input.next().equals("newLine")) {
            }
            //Get the item
            String tempString = "";
            temp = input.next();
            boolean itemNameDone = false;
            String previousForPrice = "";
            while (!temp.equals("SUBTOTAL")) {
                while (!(temp.equals("0") || temp.equals("X") || temp.equals("N"))) {
                    //I assume the code starts at 0
                    //I have no other way
                    while (!(temp.length() == 12 || (temp.length() == 14 && temp.charAt(13) == 'F')) && !itemNameDone && !temp.equals("newLine") && temp.charAt(0) != '0') {
                        tempString += temp + " ";
                        temp = input.next();
                    }
                    if (temp.length() == 12 || (temp.length() == 14 && temp.charAt(13) == 'F') || temp.charAt(0) == '0') {
                        itemNameDone = true;
                    }
                    if (!temp.equals("newLine")) {
                        previousForPrice = temp;
                    }
                    temp = input.next();
                }
                itemPrice.add(Float.parseFloat(previousForPrice));
                tempString = tempString.trim();
                itemName.add(tempString);
                tempString = "";
                itemNameDone = false;
                input.next();
                temp = input.next();
            }
            //Get the total price
            while (!temp.equals("TOTAL")) {
                temp = input.next();
            }
            input.next();
            temp = input.next();
            price = Double.parseDouble(temp);

            temp = input.next();
            //Skip until Terminal
            while (!temp.equals("TERMINAL") && !temp.equals("TERMINAL#")) {
                temp = input.next();
            }
            //Skip a line
            while (!temp.equals("newLine")) {
                temp = input.next();
            }
            date = input.next();
            input.close();
            if (itemName.size() != itemPrice.size()) {
                return "SOMETHING WRONG";
            }
            for (int i = 0; i < itemName.size(); i++) {
                Account tempAcc = ld.findOneById(id);
                History history = new History((int) db.count() + 1, itemName.get(i), itemPrice.get(i), location, date, tempAcc);
                db.save(history);
            }
            return (itemName + ", " + itemPrice + ", " + date + ", " + location).trim();
        }catch(Exception ex){
            throw new Exception("Please only send me receipt format");
        }
    }
}
