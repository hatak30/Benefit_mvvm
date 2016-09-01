package com.hatak.benefit.saldo;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

//import com.hatak.benefit.BR;

import com.hatak.benefit.BR;

import java.util.List;

/**
 * Created by hatak on 23.08.16.
 */
public class SaldoViewModel extends BaseObservable{

    private SaldoListener listener;

    private String saldoValue;

    @Bindable
    public String getSaldoValue() {
        return saldoValue;
    }

    public void setSaldoValue(final String saldoValue) {
        this.saldoValue = saldoValue;
        notifyPropertyChanged(BR.saldoValue);
    }

    public SaldoViewModel(final SaldoListener listener) {
        this.listener = listener;
    }

    public void setSaldo(final Saldo saldo) {
        setSaldoValue(saldo.getSaldo());
        listener.onTransactionsChanged(saldo.getTransactions());
    }

    public interface SaldoListener{
        void onTransactionsChanged(final List<Transaction> transactions);
    }
}
