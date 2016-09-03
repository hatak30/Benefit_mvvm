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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Saldo saldo1 = (Saldo) o;

        if (saldo != null ? !saldo.equals(saldo1.saldo) : saldo1.saldo != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = saldo != null ? saldo.hashCode() : 0;
        return result;
    }
}
