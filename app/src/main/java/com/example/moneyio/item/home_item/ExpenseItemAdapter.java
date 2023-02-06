package com.example.moneyio.item.home_item;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moneyio.databinding.ExpenseItemBinding;

import java.util.List;

public class ExpenseItemAdapter extends RecyclerView.Adapter<ExpenseItemAdapter.ExpenseItemViewHolder> {

    private final List<ExpenseItem> mList;
    private IClickListener mClickListener;

    public interface IClickListener {
        void onDeleteClickListener(ExpenseItem item);
        void onChangeClickListener(ExpenseItem item);
    }

    public ExpenseItemAdapter(List<ExpenseItem> mList, IClickListener mClickListener) {
        this.mList = mList;
        this.mClickListener = mClickListener;
    }

    @NonNull
    @Override
    public ExpenseItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ExpenseItemBinding expenseItemBinding = ExpenseItemBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ExpenseItemViewHolder(expenseItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseItemViewHolder holder, int position) {
        ExpenseItem expenseItem = mList.get(position);
        if (expenseItem == null) {
            return;
        }
        holder.expenseItemBinding.imgExpense.setImageResource(expenseItem.getExpenseImg());
        holder.expenseItemBinding.dateExpense.setText(expenseItem.getExpenseDate());
        holder.expenseItemBinding.nameExpense.setText(expenseItem.getExpenseName());
        holder.expenseItemBinding.moneyExpense.setText(Integer.toString(expenseItem.getExpenseMoney()) + "VND");

        holder.expenseItemBinding.layoutItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mClickListener.onDeleteClickListener(expenseItem);
                return false;
            }
        });

        holder.expenseItemBinding.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onChangeClickListener(expenseItem);
                return;
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mList.size() >= 5) {
            return 5;
        } else {
            return mList.size();
        }
    }

    public static class ExpenseItemViewHolder extends RecyclerView.ViewHolder {

        private final ExpenseItemBinding expenseItemBinding;

        public ExpenseItemViewHolder(@NonNull ExpenseItemBinding expenseItemBinding) {
            super(expenseItemBinding.getRoot());

            this.expenseItemBinding = expenseItemBinding;
        }
    }
}
