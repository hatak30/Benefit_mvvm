package com.hatak.benefit.login;

import com.hatak.benefit.repository.Repository;
import com.hatak.benefit.saldo.Saldo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.internal.BooleanSupplier;
import org.mockito.Mockito;
import org.mockito.internal.listeners.CollectCreatedMocks;
import org.mockito.stubbing.Answer;

import java.util.Collections;
import java.util.IllegalFormatCodePointException;

import rx.Observable;
import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * TomTom
 */
public class LoginRxPresenterTest {

    @Test
    public void shouldLoginByCredencialUsedFromRepoButSaldoFails() throws Exception {
        //Given a login presenter and viewModel mock.
        Repository mockRepository = mock(Repository.class);
        when(mockRepository.saveUser(any())).thenReturn(Observable.<Boolean>just(true));
        when(mockRepository.getSaldo(any(), any())).thenReturn(Observable.error(new SecurityException()));
        LoginPresenter loginPresenter = new LoginRxPresenter(mockRepository, Schedulers.immediate(), Schedulers.immediate());
        LoginViewModel mockLoginViewModel = Mockito.mock(LoginViewModel.class);
        //When login
        loginPresenter.onLogin(mockLoginViewModel);
        //Then view model updated
        verify(mockLoginViewModel).loginStarted();
        verify(mockLoginViewModel).getCardNumber();
        verify(mockLoginViewModel).getNikNumber();
        verify(mockLoginViewModel).setError(Mockito.any(SecurityException.class));
    }

    @Test
    public void shouldLoginByCredencialUsedFromRepoButLoginFails() throws Exception {
        //Given a login presenter and viewModel mock.
        Repository mockRepository = mock(Repository.class);
        when(mockRepository.saveUser(any())).thenReturn(Observable.<Boolean>just(false));  //TODO not mather if save user is true or false it is always success !!!
        when(mockRepository.getSaldo(any(), any())).thenReturn(Observable.just(new Saldo("1zl", Collections.emptyList())));
        LoginPresenter loginPresenter = new LoginRxPresenter(mockRepository,Schedulers.immediate(), Schedulers.immediate() );
        LoginViewModel mockLoginViewModel = Mockito.mock(LoginViewModel.class);
        //When login
        loginPresenter.onLogin(mockLoginViewModel);
        //Then view model updated
        verify(mockLoginViewModel).loginStarted();
        verify(mockLoginViewModel).getCardNumber();
        verify(mockLoginViewModel).getNikNumber();
        verify(mockLoginViewModel, times(1)).loginEnded(Mockito.any(Saldo.class)); //SHOULD BE 0 because save user fail
    }


    @Test
    public void shouldLoginByCredencialUsedFromRepoAndLoginTrue() throws Exception {
        //Given a login presenter and viewModel mock.
        Repository mockRepository = mock(Repository.class);
        when(mockRepository.saveUser(any())).thenReturn(Observable.<Boolean>just(true));
        Saldo s = new Saldo("1zl", Collections.emptyList());
        when(mockRepository.getSaldo(any(), any())).thenReturn(Observable.just(s));
        LoginPresenter loginPresenter = new LoginRxPresenter(mockRepository, Schedulers.immediate(), Schedulers.immediate());
        LoginViewModel mockLoginViewModel = Mockito.mock(LoginViewModel.class);
        //When login
        loginPresenter.onLogin(mockLoginViewModel);
        //Then view model updated
        verify(mockLoginViewModel).loginStarted();
        verify(mockLoginViewModel).getCardNumber();
        verify(mockLoginViewModel).getNikNumber();
        verify(mockLoginViewModel).loginEnded(s);
    }


    @Test
    public void onStart() throws Exception {

    }

}