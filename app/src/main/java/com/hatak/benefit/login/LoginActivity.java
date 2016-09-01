package com.hatak.benefit.login;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.hatak.benefit.R;
import com.hatak.benefit.databinding.ActivityLoginBinding;
import com.hatak.benefit.saldo.Saldo;
import com.hatak.benefit.saldo.SaldoActivity;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoginHandler, LoginViewModel.LoginListener {

    private ActivityLoginBinding binding;
    private LoginViewModel viewModel;
    private LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        viewModel = new LoginViewModel(this);
        binding.setViewModel(viewModel);
        binding.setHandler(this);
        loginPresenter = new LoginPresenter();
    }

    @Override
    public View.OnClickListener onLogin(final LoginViewModel viewModel) {
        return view -> loginPresenter.onLogin(viewModel);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loginPresenter.onStart(viewModel);
    }

    @Override
    protected void onResume() {
        super.onResume();
        animate();
        hideSoftKeyboard();
    }

    private void animate() {
        YoYo.with(Techniques.BounceIn).playOn(binding.titleImageView);
        YoYo.with(Techniques.Landing).playOn(binding.quateTextView);
        YoYo.with(Techniques.Landing).playOn(binding.loginForm);
    }

    @Override
    public void onLoginComplete(final Saldo saldo) {
        Intent intent = new Intent(this, SaldoActivity.class);
        intent.putExtra(SaldoActivity.SALDO_KEY, saldo);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onError(final Throwable throwable) {
        Snackbar.make(binding.cardNumber, R.string.unexpected_error_msg, Snackbar.LENGTH_SHORT).show();
    }

    private void hideSoftKeyboard() {
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }
}

