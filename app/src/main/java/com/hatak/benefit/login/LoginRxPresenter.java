package com.hatak.benefit.login;

import android.util.Log;

import com.hatak.benefit.repository.Repository;
import com.hatak.benefit.repository.User;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hatak on 22.08.16.
 */
public class LoginRxPresenter implements LoginPresenter {

    private Repository repository;

    public LoginRxPresenter() {
        repository = Repository.getInstance();
    }

    public LoginRxPresenter(Repository repository) {
        this.repository = repository;
    }

    public Repository getRepo(){
        return repository;
    }

    public void onLogin(final LoginViewModel viewModel) {
        viewModel.loginStarted();
        final User user = new User(viewModel.getCardNumber(), viewModel.getNikNumber());
        getRepo()
                .saveUser(user)
                .subscribeOn(Schedulers.io())
                .subscribe(aBoolean -> {
                    getRepo()
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
        getRepo()
                .loadUser()
                .subscribeOn(Schedulers.io())
                .subscribe(uiUser -> {
                    viewModel.setCardNumber(uiUser.getCardNumber());
                    viewModel.setNikNumber(uiUser.getNikNumber());
                });
    }
}
