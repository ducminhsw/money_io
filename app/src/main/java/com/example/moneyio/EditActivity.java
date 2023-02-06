package com.example.moneyio;

import static android.content.ContentValues.TAG;

import static com.example.moneyio.LoginActivity.strUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.moneyio.databinding.ActivityEditBinding;
import com.example.moneyio.item.home_item.ExpenseItem;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EditActivity extends AppCompatActivity {

    private ActivityEditBinding binding;

    private final String[] items = {"Food", "Accessory", "Home stuff", "Fun", "Side things"};
    private final List<String> itemList = new ArrayList<>();
    private final int[] imgItems = {R.drawable.food, R.drawable.accessories, R.drawable.sunny,
            R.drawable.luffy, R.drawable.sidethings};

    private int typeImage;
    private int typeExpense;
    private int currNumber = 0;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // set itemList up
        itemList.addAll(Arrays.asList(items));

        // get Data from firebase
        Bundle extras = getIntent().getExtras();
        String name = null;
        int id = -1;
        int money = -1;
        int type = -1;
        String date = null;
        int img = -1;

        if (extras != null) {
            name = extras.getString("name");
            id = extras.getInt("id");
            money = extras.getInt("money");
            type = extras.getInt("type");
            date = extras.getString("date");
            img = extras.getInt("img");
        }

        binding.edtName.setText(name);
        binding.edtVnd.setText(String.valueOf(money));

        // DatePicker
        setDatePicker(date);

        // Back button on action bar
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Choose type of expense in Add layout
        ArrayAdapter<String> adapterItems = new ArrayAdapter<>(this, R.layout.type_list_add_layout, itemList);
        binding.autoComplete.setAdapter(adapterItems);
        binding.autoComplete.setOnItemClickListener((adapterView, view, i, l) -> {
            String item = adapterView.getItemAtPosition(i).toString();
            Toast.makeText(getApplicationContext(), item, Toast.LENGTH_SHORT).show();
        });
        binding.autoComplete.setText(itemList.get(2), false);

        if(type == 0) {
            binding.reviewRg.check(R.id.rb_good);
        } else if(type == 1) {
            binding.reviewRg.check(R.id.rb_bad);
        } else if(type == 1) {
            binding.reviewRg.check(R.id.rb_ns);
        }

        // Review Radio group
        Log.d(TAG, "onCreate: Type " + type);
        binding.reviewRg.setOnCheckedChangeListener((radioGroup, i) -> {
            switch (i) {
                case R.id.rb_ns:
                    typeExpense = 2;
                    break;
                case R.id.rb_good:
                    typeExpense = 0;
                    break;
                case R.id.rb_bad:
                    typeExpense = 1;
                    break;
            }
        });

        // Save expense button
        int finalId = id;
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
                        finalId,
                        typeImage,
                        binding.edtName.getText().toString().trim(),
                        binding.edtDate.getText().toString().trim(),
                        Integer.parseInt(binding.edtVnd.getText().toString().trim()),
                        typeExpense);
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference(strUser.replace(".", "1"));

                myRef.child(String.valueOf(finalId)).updateChildren(expenseItem.toMap());
                finish();
            }
        });
    }

    private void setDatePicker(String date) {
        // SetText for date picker
        Date date1 = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        String stringDate = dateFormat.format(date1);
        binding.edtDate.setText(date);

        // Clickable Date change
        final Calendar calendar = Calendar.getInstance();
        final int y = calendar.get(Calendar.YEAR);
        final int m = calendar.get(Calendar.MONTH);
        final int d = calendar.get(Calendar.DAY_OF_MONTH);

        binding.edtDate.setOnClickListener(view -> {
            DatePickerDialog dialogFragment = new DatePickerDialog(EditActivity.this,
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
}