package com.letgro.backend.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Triet Than
 */
@Entity
@Table(name = "account")
public class Account {

    @Id
    private Integer id;

    @Column(nullable = false, unique = true)
    private String user;

//    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<ToBuy> toBuyList = new ArrayList<>();
//
//    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Receipt> receipts = new ArrayList<>();

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @Fetch(value = FetchMode.SUBSELECT)
    @JsonIgnore
    private List<ToBuy> toBuys;

    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @Fetch(value = FetchMode.SUBSELECT)
    @JsonIgnore
    private List<Receipt> receipts;


    @ManyToOne
    @JoinColumn(nullable = true)
    @JsonIgnore
    Room room;


    @OneToMany(mappedBy = "account")
    @JsonIgnore
    private List<History> histories;


    public Account() {
    }


    public Account(int id, String user, String email, String password, Room room) {
        this.id = id;
        this.user = user;
        this.email = email;
        this.password = password;
        this.toBuys = new ArrayList<>();
        this.receipts = new ArrayList<>();

        this.room = room;

        this.histories = new ArrayList<>();

    }

    public Integer getAccountId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getUser() {
        return user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAccountId(Integer id) {
        this.id = id;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //    public void setToBuyList(List<ToBuy> toBuyList) {
//        this.toBuyList = toBuyList;
//    }
//
    public void addToBuyList(ToBuy toBuy) {
        toBuys.add(toBuy);
    }

    //
//    public void addToBuyListArray(List<ToBuy> array) {
//        for(int i=0; i< array.size(); i++){
//            toBuyList.add(array.get(i));
//        }
//    }
//
    public List<ToBuy> getToBuyList() {
        return toBuys;
    }

    public List<Receipt> getReceipts() {
        return receipts;
    }

    public List<History> getHistories() {
        return histories;
    }

    public void addReceipt(Receipt receipt) {
        this.receipts.add(receipt);
    }


    public Room getRoom() {
        return this.room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public void addHistory(History history) {
        this.histories.add(history);

    }

    public boolean equals(Object account) {
        if (account == this) {
            return true;
        }
        if (!(account instanceof Account)) {
            return false;
        }
        Account a = (Account) account;

        if (room == null || a.getRoom() == null) {
            return a.getAccountId() == ((Account) account).getAccountId();
        }
        return a.getAccountId() == ((Account) account).getAccountId() && room.equals(a.getRoom())
                && toBuys.equals(a) && histories.equals(a.getHistories()) &&
                receipts.equals(a.getReceipts());
    }

    public String toString() {
        return "ID: " + getAccountId() + ", User: " + getUser() + ", Email: " + getEmail();
    }
}
