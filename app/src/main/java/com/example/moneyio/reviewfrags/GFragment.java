package com.example.moneyio.reviewfrags;

import static com.example.moneyio.LoginActivity.strUser;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moneyio.R;
import com.example.moneyio.databinding.FragmentGBinding;
import com.example.moneyio.item.home_item.ExpenseItem;
import com.example.moneyio.item.home_item.ExpenseItemAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class GFragment extends Fragment {

    private FragmentGBinding binding;
    private List<ExpenseItem> gList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentGBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initial things
        RecyclerView recyclerView = binding.rbGood;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                root.getContext(),
                RecyclerView.VERTICAL,
                false);
        recyclerView.setLayoutManager(linearLayoutManager);

        ExpenseItemAdapter itemsAdapter = new ExpenseItemAdapter(gList, new ExpenseItemAdapter.IClickListener() {
            @Override
            public void onDeleteClickListener(ExpenseItem item) {
                onClickDeleteItem(item);
            }

            @Override
            public void onChangeClickListener(ExpenseItem item) {
                onClickChangeItem(item);
            }
        });
        recyclerView.setAdapter(itemsAdapter);

        // Filtering my data
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(strUser.replace(".", "1"));

        Query query = myRef.orderByChild("expenseType").equalTo(0);
        query.addChildEventListener(new ChildEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ExpenseItem expenseItem = snapshot.getValue(ExpenseItem.class);
                if (expenseItem != null) {
                    gList.add(expenseItem);
                    itemsAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ExpenseItem expenseItem = snapshot.getValue(ExpenseItem.class);
                if (expenseItem == null || gList == null || gList.isEmpty()) {
                    return;
                }
                for (int i = 0; i < gList.size(); i++) {
                    if (expenseItem.getExpenseId() == gList.get(i).getExpenseId()) {
                        gList.set(i, expenseItem);
                        break;
                    }
                }
                itemsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                ExpenseItem expenseItem = snapshot.getValue(ExpenseItem.class);
                if (expenseItem == null || gList.size() == 0) {
                    return;
                }
                for (int i = 0; i < gList.size(); i++) {
                    if (expenseItem.getExpenseId() == gList.get(i).getExpenseId()) {
                        gList.remove(gList.get(i));
                        break;
                    }
                }
                itemsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Inflate the layout for this fragment
        return root;
    }

    private void onClickChangeItem(ExpenseItem item) {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView((R.layout.activity_edit));
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(strUser.replace(".", "1"));

        final String[] items = {"Food", "Accessory", "Home stuff", "Fun", "Side things"};
        final List<String> itemList = new ArrayList<>();
        final int[] imgItems = {R.drawable.food, R.drawable.accessories, R.drawable.sunny,
                R.drawable.luffy, R.drawable.sidethings};

        final int[] typeImage = {0};
        final AtomicInteger[] typeExpense = {new AtomicInteger()};

        EditText edtUpdateName = dialog.findViewById(R.id.edt_name);
        EditText edtUpdateVnd = dialog.findViewById(R.id.edt_vnd);
        AutoCompleteTextView autoComplete = dialog.findViewById(R.id.autoComplete);
        TextView edtUpdateDate = dialog.findViewById(R.id.edt_date);
        RadioGroup edtUpdateRadio = dialog.findViewById(R.id.review_rg);

        Button edtBtn = dialog.findViewById(R.id.save_expense_btn);


        // set itemList up
        itemList.addAll(Arrays.asList(items));

        // get Data from firebase
        edtUpdateName.setText(item.getExpenseName());
        edtUpdateVnd.setText(String.valueOf(item.getExpenseMoney()));

        // DatePicker
        setDatePicker(item.getExpenseDate(), edtUpdateDate);

        // Choose type of expense in Add layout
        ArrayAdapter<String> adapterItems = new ArrayAdapter<>(getActivity(), R.layout.type_list_add_layout, itemList);
        autoComplete.setAdapter(adapterItems);
        autoComplete.setOnItemClickListener((adapterView, view, i1, l) -> {
            String item_i1 = adapterView.getItemAtPosition(i1).toString();
            Toast.makeText(getContext(), item_i1, Toast.LENGTH_SHORT).show();
        });

        // set default value of Img:
        switch (item.getExpenseImg()) {
            case R.drawable.food:
                autoComplete.setText(itemList.get(0), false);
                break;
            case R.drawable.accessories:
                autoComplete.setText(itemList.get(1), false);
                break;
            case R.drawable.sunny:
                autoComplete.setText(itemList.get(2), false);
                break;
            case R.drawable.luffy:
                autoComplete.setText(itemList.get(3), false);
                break;
            case R.drawable.sidethings:
                autoComplete.setText(itemList.get(4), false);
                break;
        }

        // set default value of radio group
        if (item.getExpenseType() == 0) {
            edtUpdateRadio.check(R.id.rb_good);
        } else if (item.getExpenseType() == 1) {
            edtUpdateRadio.check(R.id.rb_bad);
        } else if (item.getExpenseType() == 1) {
            edtUpdateRadio.check(R.id.rb_ns);
        }

        // Review Radio group
        edtUpdateRadio.setOnCheckedChangeListener((radioGroup, i2) -> {
            switch (i2) {
                case R.id.rb_ns:
                    typeExpense[0].set(2);
                    break;
                case R.id.rb_good:
                    typeExpense[0].set(0);
                    break;
                case R.id.rb_bad:
                    typeExpense[0].set(1);
                    break;
            }
        });

        // Save expense button
        int finalId = item.getExpenseId();
        edtBtn.setOnClickListener(view -> {
            if ("".equals(edtUpdateName.getText().toString()) ||
                    "".equals(edtUpdateVnd.getText().toString()) ||
                    "".equals(autoComplete.getText().toString())) {
                return;
            } else {
                switch (autoComplete.getText().toString()) {
                    case "Food":
                        typeImage[0] = imgItems[0];
                        break;
                    case "Accessory":
                        typeImage[0] = imgItems[1];
                        break;
                    case "Home stuff":
                        typeImage[0] = imgItems[2];
                        break;
                    case "Fun":
                        typeImage[0] = imgItems[3];
                        break;
                    case "Side things":
                        typeImage[0] = imgItems[4];
                        break;
                }
                ExpenseItem expenseItem = new ExpenseItem(
                        finalId,
                        typeImage[0],
                        edtUpdateName.getText().toString().trim(),
                        edtUpdateDate.getText().toString().trim(),
                        Integer.parseInt(edtUpdateVnd.getText().toString().trim()),
                        typeExpense[0].get());

                item.setExpenseImg(expenseItem.getExpenseImg());
                item.setExpenseName(expenseItem.getExpenseName());
                item.setExpenseDate(expenseItem.getExpenseDate());
                item.setExpenseMoney(expenseItem.getExpenseMoney());
                item.setExpenseType(expenseItem.getExpenseType());

                myRef.child(String.valueOf(item.getExpenseId())).updateChildren(item.toMap(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Toast.makeText(getContext(), "Done Editing", Toast.LENGTH_SHORT);
                        dialog.dismiss();
                    }
                });
            }
        });
        dialog.show();
    }

    private void onClickDeleteItem(ExpenseItem item) {
        new AlertDialog.Builder(getContext())
                .setTitle("Delete this item?")
                .setMessage("Do you really want to delete this expense?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference(strUser.replace(".", "1"));

                        myRef.child(String.valueOf(item.getExpenseId())).removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void setDatePicker(String expenseDate, @NonNull TextView edtUpdateDate) {
        // SetText for date picker
        Date date1 = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        String stringDate = dateFormat.format(date1);
        edtUpdateDate.setText(expenseDate);

        // Clickable Date change
        final Calendar calendar = Calendar.getInstance();
        final int y = calendar.get(Calendar.YEAR);
        final int m = calendar.get(Calendar.MONTH);
        final int d = calendar.get(Calendar.DAY_OF_MONTH);

        edtUpdateDate.setOnClickListener(view -> {
            DatePickerDialog dialogFragment = new DatePickerDialog(getContext(),
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                            String monthInLetter = null;
                            String dateInNumber;

                            if (date < 10) {
                                dateInNumber = "0" + date;
                            } else dateInNumber = "" + date;
                            switch (month + 1) {
                                case 1:
                                    monthInLetter = "Jan";
                                    break;
                                case 2:
                                    monthInLetter = "Feb";
                                    break;
                                case 3:
                                    monthInLetter = "Mar";
                                    break;
                                case 4:
                                    monthInLetter = "Apr";
                                    break;
                                case 5:
                                    monthInLetter = "May";
                                    break;
                                case 6:
                                    monthInLetter = "Jun";
                                    break;
                                case 7:
                                    monthInLetter = "Jul";
                                    break;
                                case 8:
                                    monthInLetter = "Aug";
                                    break;
                                case 9:
                                    monthInLetter = "Sep";
                                    break;
                                case 10:
                                    monthInLetter = "Oct";
                                    break;
                                case 11:
                                    monthInLetter = "Nov";
                                    break;
                                case 12:
                                    monthInLetter = "Dec";
                                    break;
                            }
                            String dateInString = "" + dateInNumber + " " + monthInLetter + " " + year;
                            edtUpdateDate.setText(dateInString);
                        }
                    }, y, m, d);
            dialogFragment.show();
        });
    }
}