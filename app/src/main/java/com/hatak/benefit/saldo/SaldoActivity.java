package com.hatak.benefit.saldo;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hatak.benefit.R;
import com.hatak.benefit.databinding.ActivitySaldoBinding;

import java.util.List;

public class SaldoActivity extends AppCompatActivity implements SaldoViewModel.SaldoListener{

    public static String SALDO_KEY = "saldo.key";
    private ActivitySaldoBinding binding;
    private SaldoViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_saldo);
        viewModel = new SaldoViewModel(this);
        binding.setViewModel(viewModel);
        setupTransactionsView(binding.transactionsRecyclerView);
        setSupportActionBar(binding.toolbar);

        Saldo saldo = (Saldo) getIntent().getSerializableExtra(SALDO_KEY);
        viewModel.setSaldo(saldo);
    }

    private void setupTransactionsView(final RecyclerView recyclerView) {
        TransactionAdapter adapter = new TransactionAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onTransactionsChanged(final List<Transaction> transactions) {
        TransactionAdapter adapter = (TransactionAdapter) binding.transactionsRecyclerView.getAdapter();
        adapter.setTransactions(transactions);
        adapter.notifyDataSetChanged();
    }
}
