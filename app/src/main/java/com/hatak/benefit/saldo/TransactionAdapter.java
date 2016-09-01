package com.hatak.benefit.saldo;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.hatak.benefit.R;
import com.hatak.benefit.databinding.ItemTransactionBinding;

import java.util.Collections;
import java.util.List;

/**
 * Created by hatak on 28.08.16.
 */
public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private List<Transaction> transactions;

    public TransactionAdapter(){
        this.transactions = Collections.emptyList();
    }

    public TransactionAdapter(List<Transaction> transactions){
        this.transactions = transactions;
    }

    public void setTransactions(final List<Transaction> transactions){
        this.transactions = transactions;
    }

    @Override
    public TransactionAdapter.TransactionViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final ItemTransactionBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.item_transaction, parent, false);
        return new TransactionViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final TransactionAdapter.TransactionViewHolder holder, final int position) {
        holder.bindTransaction(transactions.get(position));
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public static class TransactionViewHolder extends RecyclerView.ViewHolder {
        final ItemTransactionBinding binding;

        public TransactionViewHolder(final ItemTransactionBinding binding) {
            super(binding.cardView);
            this.binding = binding;
        }

        void bindTransaction(final Transaction transaction){
            if (binding.getViewModel() == null) {
                binding.setViewModel(new TransactionViewModel(transaction));
            } else {
                binding.getViewModel().setTransaction(transaction);
            }
        }
    }
}
