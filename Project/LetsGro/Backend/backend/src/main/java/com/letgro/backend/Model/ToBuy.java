package com.letgro.backend.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * Entity of ToBuy The class contains id, item, and account.
 * ToBuy object is the item that our user want to buy.
 * They can remove it after they have purchased the to buy item.
 * Author: Leyuan Loh
 */
@Entity
@Table(name = "toBuy")
public class ToBuy {

    /**
     * Primary key.
     */
    @Id
    private long id;


    /**
     * A to buy item.
     */
    @Column
    private String item;

    /**
     * Whose this to buy item is belong to.
     */
    @ManyToOne
    @JoinColumn(nullable = true)
    @JsonIgnore
    Account account;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "room_id", nullable = true)
    @JsonIgnore
    Room room;


    /**
     * Constructor ToBuy class.
     * <p>
     * Id is the primary key, item is the to buy item, and account is this to buy item belongs to.
     *
     * @param id
     * @param item
     * @param account
     */
    public ToBuy(long id, String item, Account account, Room room) {
        this.id = id;
        this.item = item;
        this.account = account;
        this.room = room;
    }

    /**
     * Default empty constructor
     */
    public ToBuy() {

    }

    /**
     * Getter for id.
     *
     * @return id
     */
    public long getId() {
        return id;
    }

    /**
     * Setter for id.
     *
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Getter for item.
     *
     * @return item
     */
    public String getItem() {
        return item;
    }

    /**
     * Getter for account.
     *
     * @return account
     */
    public Account getAccount() {
        return account;
    }

    /**
     * Setter for account.
     *
     * @param account
     */
    public void setAccount(Account account) {
        this.account = account;
    }

    public Room getRoom() {
        return this.room;
    }

    public void setRoom(Room room){
        this.room = room;
    }

    /**
     * toString method for ToBuy.
     * This method return a string to represent the to buy item.
     *
     * @return String
     */
    public String toString() {
        return "ID: " + id + "\nItem: " + item;
    }

    /**
     * To check whether two toBuy object are equaled or not.
     * Two objects are equaled if and only if they have the same id, to buy item, and account.
     *
     * @param o
     * @return
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }
        ToBuy temp = (ToBuy) o;
        if ((temp.getAccount() == null || getAccount() == null) &&
                (temp.getRoom() != null && temp.getRoom() != null)) {
            return getId() == temp.getId() && getItem().equals(temp.getItem()) && getRoom().equals(temp.getRoom());
        }else if((temp.getAccount() != null && getAccount() != null) &&
                (temp.getRoom() == null || temp.getRoom() == null)){
            return getId() == temp.getId() && getItem().equals(temp.getItem()) && getAccount().equals(temp.getAccount());
        }
        return getId() == temp.getId() && getItem().equals(temp.getItem());
    }
}
