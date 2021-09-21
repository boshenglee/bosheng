package com.letgro.backend;

import com.letgro.backend.Controller.ReceiptController;
import com.letgro.backend.Controller.RoomController;
import com.letgro.backend.Database.LoginDatabase;
import com.letgro.backend.Database.RoomDatabase;
import com.letgro.backend.Database.ToBuyDatabase;
import com.letgro.backend.Model.Account;
import org.junit.Assert;
import com.letgro.backend.Model.Room;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
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

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

// import mockito related
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(RoomController.class)
public class TestingRoomControllerUnit {

    @TestConfiguration
    static class RoomContextConfiguration {
        @Bean
        public RoomDatabase getRoomDatabase() {
            return mock(RoomDatabase.class);
        }

        @Bean
        public ToBuyDatabase getToBuyDatabase() {
            return mock(ToBuyDatabase.class);
        }

        @Bean
        public LoginDatabase getLoginDatabase() {
            return mock(LoginDatabase.class);
        }
    }

    @Autowired
    private RoomDatabase rd;

    @Autowired
    private LoginDatabase ld;

    @Autowired
    private MockMvc controller;


    @Test
    public void testGetRoom() throws Exception {
        ArrayList<Room> l = new ArrayList<>();

        Room room = new Room(1);
        l.add(room);

        when(rd.findAll()).thenReturn(l);

        MvcResult mvcResult = controller.perform(get("/rooms")).andExpect(status().isOk()).andReturn();

        String actualResponseBody = mvcResult.getResponse().getContentAsString().trim();
        Assert.assertEquals("[{\"id\":1}]", actualResponseBody);


    }


    @Test
    public void testRoomCheck() throws Exception {
        ArrayList<Room> l = new ArrayList<>();
        Room room = new Room(1);
        Account account = new Account(1, "Le", "123", "lol", room);

        room.addAccount(account);

        when(ld.findOneById(1)).thenReturn(account);

        when(rd.findOneById((long)1)).thenReturn(room);

        MvcResult mvcResult = controller.perform(get("/rooms")).andExpect(status().isOk()).andReturn();

        String actualResponseBody = mvcResult.getResponse().getContentAsString().trim();
        Assert.assertEquals("[{\"id\":1}]", actualResponseBody);
    }
}
