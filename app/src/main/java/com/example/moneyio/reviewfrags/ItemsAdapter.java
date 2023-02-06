package com.example.moneyio.reviewfrags;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moneyio.databinding.ExpenseItemBinding;
import com.example.moneyio.item.home_item.ExpenseItem;

import java.util.ArrayList;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemsViewHolder>{

    private List<ExpenseItem> mList = new ArrayList<>();

    public ItemsAdapter(List<ExpenseItem> list) {
        mList = list;
    }

    @NonNull
    @Override
    public ItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ExpenseItemBinding expenseItemBinding = ExpenseItemBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ItemsViewHolder(expenseItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsViewHolder holder, int position) {
        ExpenseItem expenseItem = mList.get(position);
        if (expenseItem == null) {
            return;
        }
        holder.expenseItemBinding.imgExpense.setImageResource(expenseItem.getExpenseImg());
        holder.expenseItemBinding.dateExpense.setText(expenseItem.getExpenseDate());
        holder.expenseItemBinding.nameExpense.setText(expenseItem.getExpenseName());
        holder.expenseItemBinding.moneyExpense.setText(Integer.toString(expenseItem.getExpenseMoney()) + "VND");
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ItemsViewHolder extends RecyclerView.ViewHolder {

        private final ExpenseItemBinding expenseItemBinding;

        public ItemsViewHolder(@NonNull ExpenseItemBinding expenseItemBinding) {
            super(expenseItemBinding.getRoot());

            this.expenseItemBinding = expenseItemBinding;
        }
    }
}
