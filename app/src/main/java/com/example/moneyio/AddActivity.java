package com.example.moneyio;

import static android.content.ContentValues.TAG;
import static com.example.moneyio.LoginActivity.strUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.moneyio.item.home_item.ExpenseItem;
import com.example.moneyio.databinding.ActivityAddBinding;
import com.example.moneyio.item.home_item.IntObject;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddActivity extends AppCompatActivity {

    private ActivityAddBinding binding;

    private final String[] items = {"Food", "Accessory", "Home stuff", "Fun", "Side things"};
    private final int[] imgItems = {R.drawable.food, R.drawable.accessories, R.drawable.sunny,
            R.drawable.luffy, R.drawable.sidethings};

    private int typeImage;
    private int typeExpense;
    private int currNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // DatePicker
        setDatePicker();

        // Back button on action bar
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Choose type of expense in Add layout
        ArrayAdapter<String> adapterItems = new ArrayAdapter<>(this, R.layout.type_list_add_layout, items);
        binding.autoComplete.setAdapter(adapterItems);
        binding.autoComplete.setOnItemClickListener((adapterView, view, i, l) -> {
            String item = adapterView.getItemAtPosition(i).toString();
            Toast.makeText(getApplicationContext(), item, Toast.LENGTH_SHORT).show();
        });

        // Review Radio group
        binding.reviewRg.setOnCheckedChangeListener((radioGroup, i) -> {
            switch (i) {
                case R.id.rb_ns:
                    typeExpense = 2;
                    Toast.makeText(getApplicationContext(), "Not sure", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.rb_good:
                    typeExpense = 0;
                    Toast.makeText(getApplicationContext(), "Good", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.rb_bad:
                    typeExpense = 1;
                    Toast.makeText(getApplicationContext(), "Bad", Toast.LENGTH_SHORT).show();
                    break;
            }
        });

        // Save expense button
        binding.saveExpenseBtn.setOnClickListener(view -> {
            if ("".equals(binding.edtName.getText().toString()) ||
                    "".equals(binding.edtVnd.getText().toString()) ||
                    "".equals(binding.autoComplete.getText().toString())) {
                finish();
            } else {
                switch (binding.autoComplete.getText().toString()) {
                    case "Food":
                        typeImage = imgItems[0];
                        break;
                    case "Accessory":
                        typeImage = imgItems[1];
                        break;
                    case "Home stuff":
                        typeImage = imgItems[2];
                        break;
                    case "Fun":
                        typeImage = imgItems[3];
                        break;
                    case "Side things":
                        typeImage = imgItems[4];
                        break;
                }
                ExpenseItem expenseItem = new ExpenseItem(
                        0,
                        typeImage,
                        binding.edtName.getText().toString().trim(),
                        binding.edtDate.getText().toString().trim(),
                        Integer.parseInt(binding.edtVnd.getText().toString().trim()),
                        typeExpense);
                onClickPushData(expenseItem);
                finish();
            }
        });

    }

    private void setDatePicker() {
        // SetText for date picker
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        String stringDate = dateFormat.format(date);
        binding.edtDate.setText(stringDate);

        // Clickable Date change
        final Calendar calendar = Calendar.getInstance();
        final int y = calendar.get(Calendar.YEAR);
        final int m = calendar.get(Calendar.MONTH);
        final int d = calendar.get(Calendar.DAY_OF_MONTH);

        binding.edtDate.setOnClickListener(view -> {
            DatePickerDialog dialogFragment = new DatePickerDialog(AddActivity.this,
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
                            binding.edtDate.setText(dateInString);
                        }
                    }, y, m, d);
            dialogFragment.show();
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onClickPushData(ExpenseItem expenseItem) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(strUser.replace(".", "1"));

        myRef.child("number").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                IntObject io_b = snapshot.getValue(IntObject.class);
                if (io_b != null) {
                    int number = io_b.getNumber();
                    io_b.setNumber(++number);
                    myRef.child("number").setValue(io_b);
                    currNumber = io_b.getNumber();
                    Log.d(TAG, "onDataChange: in != null " + currNumber);
                } else {
                    IntObject io_b2 = new IntObject(1);
                    myRef.child("number").setValue(io_b2);
                    currNumber = io_b2.getNumber();
                    Log.d(TAG, "onDataChange: in == null " + currNumber);
                }
                String pathObject = String.valueOf(currNumber);
                expenseItem.setExpenseId(currNumber);
                myRef.child(pathObject).setValue(expenseItem);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}