package com.letgro.backend.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "rooms")
public class Room {
    @Id
    long id;

    @OneToMany(mappedBy = "room", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @Fetch(value = FetchMode.SUBSELECT)
    @JsonIgnore
    private List<Account> accounts;

    @OneToMany(mappedBy = "room", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @Fetch(value = FetchMode.SUBSELECT)
    @JsonIgnore
    private List<ToBuy> toBuys;

    public Room(long id) {
        this.id = id;
        accounts = new ArrayList<>();
        toBuys = new ArrayList<>();
    }

    public Room() {

    }

    public void addAccount(Account account) {
        this.accounts.add(account);
    }

    public void addTobuy(ToBuy toBuy) {
        this.toBuys.add(toBuy);
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public List<ToBuy> getToBuys() {
        return toBuys;
    }

    public Long getId() {
        return this.id;
    }


    public String toString() {

        return "Id: " + id + "\n Accounts: " + accounts + "\n ToBuys" + toBuys;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || o.getClass() != getClass()) {
            return false;
        }

        Room temp = (Room) o;

        return id == temp.getId() && accounts.equals(temp.getAccounts()) && toBuys.equals(temp.getToBuys());
    }
}
