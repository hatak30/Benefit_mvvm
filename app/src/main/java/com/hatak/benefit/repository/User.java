package com.hatak.benefit.repository;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by hatak on 12.08.16.
 */
public class User extends RealmObject {

    public static final int DEFAULT_ID = 1;

    @PrimaryKey
    private int id = DEFAULT_ID;
    private String cardNumber;
    private String nikNumber;

    public User(){

    }

    public User(final String cardNumber, final String nikNumber) {
        this.cardNumber = cardNumber;
        this.nikNumber = nikNumber;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getNikNumber() {
        return nikNumber;
    }
}
