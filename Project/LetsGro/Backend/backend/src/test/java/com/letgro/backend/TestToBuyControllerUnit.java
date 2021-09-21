package com.letgro.backend;

import com.letgro.backend.BackendApplication;
import com.letgro.backend.Controller.ReceiptController;
import com.letgro.backend.Controller.ToBuyController;
import com.letgro.backend.Database.LoginDatabase;
import com.letgro.backend.Database.ToBuyDatabase;
import com.letgro.backend.Model.Account;
import com.letgro.backend.Model.Receipt;
import com.letgro.backend.Model.ToBuy;
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

import org.springframework.boot.test.context.SpringBootTest;
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
 * Test Post method for ToBuyController
 */
@RunWith(SpringRunner.class)
@WebMvcTest(ToBuyController.class)
public class TestToBuyControllerUnit {

    @TestConfiguration
    static class ToBuyContextConfiguration {
        @Bean
        public ToBuyDatabase getDatabase() {
            return mock(ToBuyDatabase.class);
        }

        @Bean
        public LoginDatabase getLoginDatabase(){
            return mock(LoginDatabase.class);
        }
    }

    @Autowired
    private MockMvc controller;

    @Autowired
    private ToBuyDatabase repo;

    @Autowired
    private LoginDatabase ld;

//    @Test
//    public void testDeleteMethod() throws Exception {
//        List<ToBuy> l = new ArrayList<ToBuy>();
//        ToBuy toBuy = new ToBuy(1, "Banana");
//        l.add(toBuy);
//        l.add(new ToBuy(2, "Milk"));
//        l.add(new ToBuy(3, "Chocolate"));
//        l.add(new ToBuy(4, "Ice cream"));
//
//        // mock the findAll method
//        when(repo.findAll()).thenReturn(l);
//
//
//
//        // mock the findOneById method
//        when(repo.findOneById((long) 0)).thenReturn(l.get(0));
//
//        // mock delete
//        when(repo.delete( toBuy)).thenAnswer();
//
//        MvcResult mvcResult = controller.perform(delete("/toBuyItem/1").contentType(MediaType.APPLICATION_JSON).content(toJson(toBuy)))
//                .andExpect(status().isOk())
//                .andReturn();
//        String actualResponseBody = mvcResult.getResponse().getContentAsString().trim();
//        Assert.assertEquals("deleted 1", actualResponseBody);
//    }

    @Test
    public void testPostMethod() throws Exception {
        List<ToBuy> l = new ArrayList<ToBuy>();

        Account account = new Account(1,"Lee","Lee@gmail.com","123",null);

        //mock the findOneById method of loginDatabase
        when(ld.findOneById(1)).thenReturn(account);

        // mock the findAll method
        when(repo.findAll()).thenReturn(l);


        //mock the count method
        when(repo.count()).thenReturn((long) l.size());

        // mock the save() method to save argument to the list
        when(repo.save( any(ToBuy.class)))
                .thenAnswer(x -> {
                    ToBuy r = x.getArgument(0);
                    l.add(r);
                    return null;
                });
        ToBuy toBuy = new ToBuy(1, "Banana",null,null);
        MvcResult mvcResult = controller.perform(post("/toBuyItem").contentType(MediaType.APPLICATION_JSON).content("{\"id\":1, \"item\": \"Banana\"}"))
                .andExpect(status().isOk())
                .andReturn();

        String actualResponseBody = mvcResult.getResponse().getContentAsString().trim();
        Assert.assertEquals("{\"id\":1,\"item\":\"Banana\"}", actualResponseBody);
        Assert.assertEquals(1, l.size());
        Assert.assertEquals(true, toBuy.equals(l.get(0)));
    }
}
