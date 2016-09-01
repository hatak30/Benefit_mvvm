package com.hatak.benefit.saldo;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.hatak.benefit.BR;

/**
 * Created by hatak on 28.08.16.
 */
public class TransactionViewModel extends BaseObservable{

    private String price;
    private String date;
    private String type;
    private String place;

    public TransactionViewModel(final Transaction transaction) {
        setTransaction(transaction);
    }

    @Bindable
    public String getPrice() {
        return price;
    }

    @Bindable
    public String getDate() {
        return date;
    }

    @Bindable
    public String getType() {
        return type;
    }

    @Bindable
    public String getPlace() {
        return place;
    }

    public void setTransaction(final Transaction transaction) {
        this.price = transaction.getPrice();
        this.date = transaction.getDate();
        this.type= transaction.getType();
        this.place = transaction.getPlace();
        notifyPropertyChanged(BR.date);
        notifyPropertyChanged(BR.place);
        notifyPropertyChanged(BR.type);
        notifyPropertyChanged(BR.price);
    }
}
