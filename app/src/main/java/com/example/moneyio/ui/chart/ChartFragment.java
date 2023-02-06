package com.example.moneyio.ui.chart;

import static android.content.ContentValues.TAG;
import static com.example.moneyio.LoginActivity.strUser;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.moneyio.databinding.FragmentChartBinding;
import com.example.moneyio.item.home_item.ExpenseItem;
import com.example.moneyio.item.home_item.IntObject;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChartFragment extends Fragment {

    private FragmentChartBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ChartViewModel chartViewModel =
                new ViewModelProvider(this).get(ChartViewModel.class);

        binding = FragmentChartBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textChart;
        chartViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        IntObject sizeG = new IntObject(0);
        IntObject sizeB = new IntObject(0);
        IntObject sizeN = new IntObject(0);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(strUser.replace(".", "1"));

        getSizeOfGood(myRef, sizeG);
        getSizeOfGood(myRef, sizeB);
        getSizeOfGood(myRef, sizeN);

        return root;
    }

    private void getSizeOfGood(DatabaseReference myRef, IntObject intObject) {
        Query query = myRef.orderByChild("expenseType").equalTo(1);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ExpenseItem expenseItem = snapshot.getValue(ExpenseItem.class);
                if(expenseItem == null) return;
                Log.d(TAG, "onChildAdded: added");
                Log.d(TAG, "onChildAdded: " + intObject.getNumber());
                intObject.setNumber(intObject.getNumber() + 1);
                Log.d(TAG, "onChildAdded: " + intObject.getNumber());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Log.d(TAG, "getSizeOfGood: " + intObject.getNumber());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}