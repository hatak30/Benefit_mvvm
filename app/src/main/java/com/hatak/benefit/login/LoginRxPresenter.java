package com.hatak.benefit.login;

import com.hatak.benefit.repository.Repository;
import com.hatak.benefit.repository.User;
import com.hatak.benefit.saldo.Saldo;

import rx.Observable;
import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by hatak on 22.08.16.
 */
public class LoginRxPresenter implements LoginPresenter {

    private static final String TAG = "LoginRxPresenter";
    private Repository repository;
    private Scheduler background = Schedulers.io();
    private Scheduler main = AndroidSchedulers.mainThread();

    public LoginRxPresenter() {
        repository = Repository.getInstance();
    }

    public LoginRxPresenter(Repository repository, Scheduler backgroundScheduler, Scheduler mainScheduler) {
        this.repository = repository;
        this.background = backgroundScheduler;
        this.main = mainScheduler;
    }

    public LoginRxPresenter(Repository repository) {
        this.repository = repository;
    }

    public Repository getRepo() {
        return repository;
    }

    public void onLogin(final LoginViewModel viewModel) {
        viewModel.loginStarted();
        final User user = new User(viewModel.getCardNumber(), viewModel.getNikNumber());
        getRepo()
                .saveUser(user)
                .subscribeOn(background)
                .observeOn(main)
                .subscribe(aBoolean -> {
                    getRepo()
                            .getSaldo(user.getCardNumber(), user.getNikNumber())
                            .subscribeOn(background)
                            .observeOn(main)
                            .subscribe(saldo -> {
                                viewModel.loginEnded(saldo);
                            }, saldoError -> viewModel.setError(saldoError));
                },userError -> viewModel.setError(userError));
    }

    public void onStart(final LoginViewModel viewModel) {
        viewModel.formStarted();
        getRepo()
                .loadUser()
                .subscribeOn(main)
                .subscribe(uiUser -> {
                    viewModel.setCardNumber(uiUser.getCardNumber());
                    viewModel.setNikNumber(uiUser.getNikNumber());
                });
    }
}
