package com.hatak.benefit.saldo;

import java.io.Serializable;

/**
 * Created by hatak on 23.08.16.
 */
public class Transaction implements Serializable{
    private String price;
    private String date;
    private String type;
    private String place;

    public String getPrice() {
        return price;
    }

    public String getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public String getPlace() {
        return place;
    }

    public void setPrice(final String price) {
        this.price = price;
    }

    public void setDate(final String date) {
        this.date = date;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public void setPlace(final String place) {
        this.place = place;
    }
}
