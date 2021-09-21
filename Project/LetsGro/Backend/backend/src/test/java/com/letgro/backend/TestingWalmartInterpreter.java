package com.letgro.backend;

import ch.qos.logback.core.encoder.EchoEncoder;
import com.letgro.backend.Database.HistoryDatabase;
import com.letgro.backend.Database.LoginDatabase;
import com.letgro.backend.Model.History;
import com.letgro.backend.Service.ReceiptInterpreter.Walmart;

import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

@RunWith(SpringRunner.class )
public class TestingWalmartInterpreter {

    String ocrText1 = "{\"data\":\"Give us feedback@survery.walmart.com\\nThank you! ID #: 7P9TCP1H2SXR\\nWalmart\\n515-956-3536 Mgr: JONATHAN\\n534 S DUFF AVE\\nAMES IA 50010\\nST# 04256 OP# 004927 TE# 04 TR# O5503\\nCANTALOUPE 000000004050KF\\nAT\\n1 FOR\\n1.93\\n3.86\\nN\\nGV WHT VNGR\\n007874235255 F\\n2.64 0\\n80 CHK GB\\n007874226969 F\\n14.86 0\\nWHOLE MILK\\n007874235186 F\\n2.34 0\\nEGGS 18CT\\n007874212708 F\\n1.57 0\\nDRUMSTICK\\n007562055872 F\\n4.86 0\\nSUBTOTAL\\n30.13\\nTOTAL\\n30.13\\nDISCV TEND\\n30.13\\nDiscover Credit\\n*** ** ********| 1\\nAPPROVAL # 00521R\\nREF # 025000346657\\nAID A000001523010\\nAAC EA2942C1C07FFF16\\nTERMINAL # SCO10388\\n09\\/05\\/20\\n19:19:46\\nCHANGE DUE\\n0.00\\n# ITEMS SOLD\\nTCM 6568 8942 8290 9627 4498\\nLow Prices You Can Trust. Every Day.\\n09\\/05\\/20\\n19:19:46\\n***CUSTOMER COPY***\\n\"}";
    String ocrText2 = "{\"data\":\"Give us feedback @ survey.walmart.com\\nThank you! ID #:7P9HSW1H20L\\nWalmart\\n515-956-3536 Mgr:\\/ONATHAN\\n534 S DUFF AVE\\nAMES IA 50010\\nST# 04256 OP# 004917 TE# 03 TR# 01747\\n80\\/20 GRD-BF 007874226960 F\\n7.90 0\\nDRUMSTICK 007562055872 F\\n4.86 0\\nGV TH HML 007874205302 F\\n15.22 0\\nCAN OPENER 007675333075\\n2.34 X\\nBOX GARLIC 007\\n690 00336 F\\n1.28 N\\nBANANAS\\n000000004011KF\\n2.02 lb@ 1 lb \\/0.47\\n0.95 N\\n3 CLEM\\n068113130530 F\\n4.34 N\\nVIT D MILK\\n007104310440 F\\n4.62 0\\nBLACK PEPPER 007874225238 F\\n4.98 0\\nWHOLE CORN 003710003662 F\\n0.76 0\\nWHOLE CORN 003710003662 F\\n.76 0\\nPEAS CARROT 002400016311 F\\n1.48 0\\nPEAS CARROT 002400016311 F\\n1.48 0\\nMRTON PL SAL 002460001001 F\\n0.92 0\\nJUMP ROPE 004288742843\\n3.98 X\\nBE SF BRCAUL 001450002132 F\\n1.97 0\\nRED UNION 000000004082KF\\n0.84 Ib 1 lb \\/0.98\\n0.82 N\\nSUBTOTAL\\n58.7 74\\nTAX 1 7.000%\\n0.4 14\\nTOTAL\\n59.18\\nDISCV TEND\\n59.18\\nDiscover Credit * ** ** ***1 | 1\\nAPPROVAL # 01363R\\nREF # 022600795297\\nAID A0000001523010\\nAAC C937517363F46D18\\nTERMINAL # SC010053\\n08\\/13\\/20 15:29:35\\nCHANGE DUE\\n0.00\\n#ITEMS SOLD 17\\nTC# 0247 7670 7952 5149 8637\\nLow Prices You Can Trust. Every Day.\\n08\\/13\\/20\\n15:29:36\\n***CUSTOMER COPY***\\n\"}";
    String ocrText3 = "{\"data\":\"Give us feedback @ survey.walmart.com\\nThank you! ID #:7P9K2G1H2063\\nWalmart\\n515-956-3536 Mgr:JONATHAN\\n534 S DUFF AVE\\nAMES IA 50010\\nST# 04256 OP# 009030 TE# 30 TR# 02879\\nBEGBUG FLEA 007112196548\\n6.67\\nX\\nBESF FS CORN 001450001407 F\\n1.97 0\\nBE BROC\\/CR\\/S 001450002130 F\\n1.97 0\\nBE BROC\\/CR\\/S 001450002130 F\\n1.97 0\\nSUBTOTAL\\n12.58\\nTAX 1 7.000%\\n0.47\\nTOTAL\\n13.05\\nDISCV TEND\\n13.05\\nDiscover Credit * ** *******1 1 1\\nAPPROVAL # 02523R\\nREF #02380053829\\nAID A0O00001523010\\nAAC OAC9F437BOE998CA\\nTERMINAL# SC011389\\n08\\/25\\/20 08:24:26\\nCHANGE DUE\\n0.00\\n# ITEMS SOLD 4\\nTC# 0247 7670 7952 5149 8637\\nLow Prices You Can Trust. Every Day.\\n08\\/25\\/20 08:24:26\\n***CUSTOMER COPY***\\n\"}";
    String ocrText4 ="Hello";

    @TestConfiguration
    static class HistoryContextConfiguration{

        @Bean
        public Walmart walmartService(){
            return new Walmart();
        }

        @Bean
        public HistoryDatabase getDatabase(){
            return mock(HistoryDatabase.class);
        }

        @Bean
        public LoginDatabase getLoginDatabase(){
            return mock(LoginDatabase.class);
        }
    }

    @Autowired
    private Walmart walmart;

    @Test
    public void testWalmartReceipt1() throws Exception {
        String expected= "[CANTALOUPE, GV WHT VNGR, 80 CHK GB, WHOLE MILK, EGGS 18CT, DRUMSTICK], [3.86, 2.64, 14.86, 2.34, 1.57, 4.86], 09/05/20, 534 S DUFF AVE AMES IA 50010";
        assertEquals(expected,walmart.extract(ocrText1, 1));
    }

    @Test
    public void testWalmartReceipt2() throws Exception {
        String expected= "[80/20 GRD-BF, DRUMSTICK, GV TH HML, CAN OPENER, BOX GARLIC, BANANAS, 3 CLEM, VIT D MILK, BLACK PEPPER, WHOLE CORN, WHOLE CORN, PEAS CARROT, PEAS CARROT, MRTON PL SAL, JUMP ROPE, BE SF BRCAUL, RED UNION], [7.9, 4.86, 15.22, 2.34, 1.28, 0.95, 4.34, 4.62, 4.98, 0.76, 0.76, 1.48, 1.48, 0.92, 3.98, 1.97, 0.82], 08/13/20, 534 S DUFF AVE AMES IA 50010";
        assertEquals(expected,walmart.extract(ocrText2,1));
    }

    @Test
    public void testWalmartReceipt3() throws Exception {
        String expected= "[BEGBUG FLEA, BESF FS CORN, BE BROC/CR/S, BE BROC/CR/S], [6.67, 1.97, 1.97, 1.97], 08/25/20, 534 S DUFF AVE AMES IA 50010";
        assertEquals(expected,walmart.extract(ocrText3,1));
    }

    @Test(expected = Exception.class)
    public void testWalmartReceipt4() throws Exception {
        walmart.extract(ocrText4,1);
    }
}
