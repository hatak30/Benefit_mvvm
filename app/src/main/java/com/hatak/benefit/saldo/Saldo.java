package com.hatak.benefit.saldo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hatak on 23.08.16.
 */
public class Saldo implements Serializable{

    private final String saldo;
    private final List<Transaction> transactions;

    public Saldo(final String saldo, final List<Transaction> transactions) {
        this.saldo = saldo;
        this.transactions = transactions;
    }

    public String getSaldo() {
        return saldo;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
}
