package com.letgro.backend;

import com.letgro.backend.Controller.ReceiptController;
import com.letgro.backend.Database.HistoryDatabase;
import com.letgro.backend.Database.LoginDatabase;
import com.letgro.backend.Database.RoomDatabase;
import com.letgro.backend.Database.ToBuyDatabase;
import com.letgro.backend.Model.Account;
import com.letgro.backend.Model.History;
import com.letgro.backend.Model.Room;
import com.letgro.backend.Model.ToBuy;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import org.hibernate.Hibernate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.swing.*;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
       SpringApplication.run(BackendApplication.class, args);
    }
}
