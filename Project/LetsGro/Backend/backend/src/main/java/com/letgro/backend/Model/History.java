package com.letgro.backend.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * Author: Triet Than
 */

@Entity
public class History {

    @Id
    private Integer id;

    @Column(nullable = false)
    private String item;

    @Column(nullable = false)
    private Float price;

    @Column(nullable = false)
    private String store;

    @Column(nullable = false)
    private String groDate;

    @ManyToOne
    @JoinColumn(nullable = true)
    @JsonIgnore
    Account account;

    public History() {
    }

    public History(int id, String item, float price, String store, String groDate, Account account) {
        this.id = id;
        this.item = item;
        this.price = price;
        this.store = store;
        this.groDate = groDate;
        this.account = account;

    }


    public Integer getId() {
        return id;
    }

    public String getItem() {
        return item;
    }

    public float getPrice() {
        return price;
    }

    public String getStore() {
        return store;
    }

    public String getGroDate() {
        return groDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setGroDate(String groDate) {
        this.groDate = groDate;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }


    public String toString() {
        return "ID: " + getId() + ", Item: " + getItem() + ", Price: " + getPrice() + ", Store: " + getStore() + ", Date: " + getGroDate() + ", Account:" + getAccount();
    }
}
