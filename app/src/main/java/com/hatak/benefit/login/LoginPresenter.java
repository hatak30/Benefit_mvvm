package com.hatak.benefit.login;

import android.util.Log;

import com.hatak.benefit.repository.Repository;
import com.hatak.benefit.repository.User;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hatak on 22.08.16.
 */
public class LoginPresenter {

    public void onLogin(final LoginViewModel viewModel) {
        viewModel.loginStarted();
        final User user = new User(viewModel.getCardNumber(), viewModel.getNikNumber());
        Repository.getInstance()
                .saveUser(user)
                .subscribeOn(Schedulers.io())
                .subscribe(aBoolean -> {
                    Repository.getInstance()
                            .getSaldo(user.getCardNumber(), user.getNikNumber())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(saldo -> {
                                viewModel.loginEnded(saldo);
                            }, saldoError -> viewModel.setError(saldoError));
                },userError -> viewModel.setError(userError));
    }

    public void onStart(final LoginViewModel viewModel) {
        viewModel.formStarted();
        Repository.getInstance()
                .loadUser()
                .subscribeOn(Schedulers.io())
                .subscribe(uiUser -> {
                    viewModel.setCardNumber(uiUser.getCardNumber());
                    viewModel.setNikNumber(uiUser.getNikNumber());
                });
    }
}
