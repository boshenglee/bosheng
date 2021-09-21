package com.letgro.backend;

import com.letgro.backend.Controller.ReceiptController;
import com.letgro.backend.Controller.ToBuyController;
import com.letgro.backend.Database.HistoryDatabase;
import com.letgro.backend.Database.LoginDatabase;
import com.letgro.backend.Database.ReceiptDatabase;
import com.letgro.backend.Database.ToBuyDatabase;
import com.letgro.backend.Model.Account;
import com.letgro.backend.Model.History;
import com.letgro.backend.Model.Receipt;
import com.letgro.backend.Model.ToBuy;
import com.letgro.backend.Service.ReceiptInterpreter.Walmart;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

// Import Java libraries
import java.util.List;
import java.util.ArrayList;

// import junit/spring tests
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

// import mockito related
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



/**
 * Author: Leyuan Loh
 * Test Post method for ReceiptController
 */
@RunWith(SpringRunner.class)
@WebMvcTest(ReceiptController.class)
public class TestReceiptControllerUnit {

    @TestConfiguration
    static class ToBuyContextConfiguration {
        @Bean
        public ReceiptDatabase getDatabase() {
            return mock(ReceiptDatabase.class);
        }

        @Bean
        public Walmart getWalmart(){
            return new Walmart();
        }

        @Bean
        public HistoryDatabase getHistoryDatabase(){
            return mock(HistoryDatabase.class);
        }

        @Bean
        public LoginDatabase getLoginDatabase(){
            return mock(LoginDatabase.class);
        }

    }

    @Autowired
    private MockMvc controller;

    @Autowired
    private LoginDatabase ld;

    @Autowired
    private ReceiptDatabase repo;

    @Autowired
    private HistoryDatabase hd;

    @Test
    public void testPostReceiptMethod() throws Exception {
        List<Receipt> l = new ArrayList<Receipt>();

        Account account = new Account(1,"Lee","Lee@gmail.com","123",null);

        // mock the findAll method
        when(repo.findAll()).thenReturn(l);

        //mock the findOne
        when(ld.findOneById(1)).thenReturn(account);

        //mock the count method
        when(repo.count()).thenReturn((long) l.size());

        // mock the save() method to save argument to the list
        when(repo.save((Receipt) any(Receipt.class)))
                .thenAnswer(x -> {
                    Receipt r = x.getArgument(0);
                    l.add(r);
                    return null;
                });
        Receipt receipt = new Receipt(1, "img/102020.txt",null);
        MvcResult mvcResult = controller.perform(post("/receipt/account/1").contentType(MediaType.APPLICATION_JSON).content("{\"id\": 1, \"imgLocation\":\"img/102020.txt\"}"))
                .andExpect(status().isOk())
                .andReturn();

        String actualResponseBody = mvcResult.getResponse().getContentAsString().trim();
        Assert.assertEquals("{\"id\":1,\"imgLocation\":\"img/102020.txt\"}", actualResponseBody);
        Assert.assertEquals(1, l.size());
        Assert.assertEquals(true, receipt.equals(l.get(0)));
    }

    @Test
    public void testPostOcrTextMethod() throws Exception{
        List<History> l = new ArrayList<History>();
        List<Account> a = new ArrayList<>();

        Account account = new Account(1,"Hisoka","qe","123",null);

        a.add(account);

        when(ld.findOneById(1)).thenReturn(a.get(0));

        when(hd.findAll()).thenReturn(l);


        //mock the count method
        when(hd.count()).thenReturn((long) l.size());

        // mock the save() method to save argument to the list
        when(hd.save((History) any(History.class)))
                .thenAnswer(x -> {
                    History r = x.getArgument(0);
                    l.add(r);
                    return null;
                });

        String ocrText ="Give us feedback@survery.walmart.com\\nThank you! ID #: 7P9TCP1H2SXR\\nWalmart\\n515-956-3536 Mgr: JONATHAN\\n534 S DUFF AVE\\nAMES IA 50010\\nST# 04256 OP# 004927 TE# 04 TR# O5503\\nCANTALOUPE 000000004050KF\\nAT\\n1 FOR\\n1.93\\n3.86\\nN\\nGV WHT VNGR\\n007874235255 F\\n2.64 0\\n80 CHK GB\\n007874226969 F\\n14.86 0\\nWHOLE MILK\\n007874235186 F\\n2.34 0\\nEGGS 18CT\\n007874212708 F\\n1.57 0\\nDRUMSTICK\\n007562055872 F\\n4.86 0\\nSUBTOTAL\\n30.13\\nTOTAL\\n30.13\\nDISCV TEND\\n30.13\\nDiscover Credit\\n*** ** ********| 1\\nAPPROVAL # 00521R\\nREF # 025000346657\\nAID A000001523010\\nAAC EA2942C1C07FFF16\\nTERMINAL # SCO10388\\n09\\/05\\/20\\n19:19:46\\nCHANGE DUE\\n0.00\\n# ITEMS SOLD\\nTCM 6568 8942 8290 9627 4498\\nLow Prices You Can Trust. Every Day.\\n09\\/05\\/20\\n19:19:46\\n***CUSTOMER COPY***\\n";
        MvcResult mvcResult = controller.perform(post("/receipt/ocrText/account/1").contentType(MediaType.APPLICATION_JSON).content(ocrText))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString().trim();
        String expected = "[CANTALOUPE, GV WHT VNGR, 80 CHK GB, WHOLE MILK, EGGS 18CT, DRUMSTICK], [3.86, 2.64, 14.86, 2.34, 1.57, 4.86], 09/05/20, 534 S DUFF AVE AMES IA 50010";
        Assert.assertEquals(expected, actualResponseBody);
        Assert.assertEquals(6,l.size());
    }
}
