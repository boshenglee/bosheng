package com.letgro.backend.Model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;


/**
 * Entity of receipt. The class contains id, imgLocation and account. The imgLocation is the location of
 * the image in the account's device storage. Whenever our user takes a picture of their receipt, frontend will
 * send the backend a request to save the imgLocation to the database.
 * Author: Leyuan Loh
 */
@Entity
public class Receipt {
    /**
     * Primary key.
     */
    @Id
    private long id;

    /**
     * Location for the img in the file.
     * This is needed to output the image in the frontend.
     */
    @Column
    private String imgLocation;

    /**
     * The account is the owner of this receipt object.
     * It is important to have account as each user has a different imgLocation.
     */
    @ManyToOne
    @JoinColumn(nullable = true)
    @JsonIgnore
    Account account;

    /**
     * Constructor for receipt.
     *
     * @param id
     * @param imgLocation
     * @param account
     */
    public Receipt(long id, String imgLocation, Account account) {
        this.id = id;
        this.imgLocation = imgLocation;
        this.account = account;
    }

    /**
     * Empty constructor.
     */
    public Receipt() {

    }


    /**
     * Getter for getImgLocation.
     *
     * @return imgLocation
     */
    public String getImgLocation() {
        return imgLocation;
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
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Setter for getImgLocation.
     *
     * @param imgLocation
     */
    public void setImgLocation(String imgLocation) {
        this.imgLocation = imgLocation;
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

    /**
     * To check whether two receipt object are equaled or not.
     * If two receipt objects are equal if and only if they have the same id, ImgLocation, and account.
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
        Receipt temp = (Receipt) o;
        if (getAccount() == null || temp.getAccount() == null){
            return getId() == temp.getId() && getImgLocation().equals(temp.getImgLocation());
        }
            return getId() == temp.getId() && getImgLocation().equals(temp.getImgLocation()) && getAccount().equals(temp.getAccount());
    }
}
