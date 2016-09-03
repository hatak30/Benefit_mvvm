package com.hatak.benefit.login;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import com.hatak.benefit.BR;
import com.hatak.benefit.saldo.Saldo;

/**
 * Created by hatak on 12.08.16.
 */
public class LoginViewModel extends BaseObservable{

    private int loginFormVisibility = View.VISIBLE;
    private int progressVisibility = View.GONE;

    private String cardNumber;
    private String nikNumber;

    private final LoginListener loginListener;

    public LoginViewModel(final LoginListener loginListener){
        this.loginListener = loginListener;
    }

    @Bindable
    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(final String cardNumber) {
        this.cardNumber = cardNumber;
        notifyPropertyChanged(BR.cardNumber);
    }

    @Bindable
    public String getNikNumber() {
        return nikNumber;
    }

    public void setNikNumber(final String nikNumber) {
        this.nikNumber = nikNumber;
        notifyPropertyChanged(BR.nikNumber);
    }

    @Bindable
    public int getLoginFormVisibility() {
        return loginFormVisibility;
    }

    @Bindable
    public int getProgressVisibility() {
        return progressVisibility;
    }

    public void setLoginFormVisibility(final int loginFormVisibility) {
        this.loginFormVisibility = loginFormVisibility;
        notifyPropertyChanged(BR.loginFormVisibility);
    }

    public void setProgressVisibility(final int progressVisibility) {
        this.progressVisibility = progressVisibility;
        notifyPropertyChanged(BR.progressVisibility);
    }

    public void loginStarted() {
        setLoginFormVisibility(View.GONE);
        setProgressVisibility(View.VISIBLE);
    }

    public void loginEnded(final Saldo saldo) {
        loginListener.onLoginComplete(saldo);
    }

    public void formStarted(){
        setProgressVisibility(View.GONE);
        setLoginFormVisibility(View.VISIBLE);
    }

    public void setError(final Throwable saldoError) {
    }

    public interface LoginListener {
        void onLoginComplete(final Saldo saldo);

        void onError(final Throwable throwable);
    }
}


