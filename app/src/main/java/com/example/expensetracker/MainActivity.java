package com.example.expensetracker;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.ArrayList;
import com.example.expensetracker.DBHelper;
public class MainActivity extends AppCompatActivity {

    EditText amountInput, categoryInput;
    Spinner typeSpinner, currencySpinner;
    Button addBtn, deleteAllBtn, toggleThemeBtn;
    ListView transactionList;
    TextView totalText;
    DBHelper dbHelper;
    ArrayAdapter<String> listAdapter;
    ArrayList<String> listData;
    double totalAmount = 0.0;
    String currentCurrency = "INR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DBHelper(this);

        amountInput = findViewById(R.id.amountInput);
        categoryInput = findViewById(R.id.categoryInput);
        typeSpinner = findViewById(R.id.typeSpinner);
        currencySpinner = findViewById(R.id.currencySpinner);
        addBtn = findViewById(R.id.addBtn);
        deleteAllBtn = findViewById(R.id.deleteAllBtn);
        toggleThemeBtn = findViewById(R.id.toggleThemeBtn);
        transactionList = findViewById(R.id.transactionList);
        totalText = findViewById(R.id.totalText);

        listData = new ArrayList<>();
        listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        transactionList.setAdapter(listAdapter);

        String[] types = {"Income", "Expense"};
        typeSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, types));

        String[] currencies = {"INR", "USD", "EUR"};
        currencySpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, currencies));

        loadTransactions();

        addBtn.setOnClickListener(v -> {
            String amountStr = amountInput.getText().toString();
            String category = categoryInput.getText().toString();
            String type = typeSpinner.getSelectedItem().toString();

            if (amountStr.isEmpty() || category.isEmpty()) {
                Toast.makeText(this, "All fields required", Toast.LENGTH_SHORT).show();
                return;
            }

            double amount = Double.parseDouble(amountStr);
            if (type.equals("Expense")) amount = -amount;

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("type", type);
            cv.put("amount", amount);
            cv.put("category", category);
            cv.put("currency", currentCurrency); // FIXED: This is required by your DB schema
            cv.put("date", System.currentTimeMillis());
            db.insert("transactions", null, cv);

            Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show();
            amountInput.setText("");
            categoryInput.setText("");
            loadTransactions();
        });

        deleteAllBtn.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Delete All")
                    .setMessage("Are you sure you want to delete all history?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        db.delete("transactions", null, null);
                        loadTransactions();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        transactionList.setOnItemLongClickListener((parent, view, position, id) -> {
            new AlertDialog.Builder(this)
                    .setTitle("Delete")
                    .setMessage("Delete this transaction?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        Cursor cursor = db.rawQuery("SELECT id FROM transactions", null);
                        if (cursor.moveToPosition(position)) {
                            int deleteId = cursor.getInt(0);
                            db.delete("transactions", "id=?", new String[]{String.valueOf(deleteId)});
                            loadTransactions();
                        }
                        cursor.close();
                    })
                    .setNegativeButton("No", null)
                    .show();
            return true;
        });

        currencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentCurrency = currencies[position];
                loadTransactions();
            }

            public void onNothingSelected(AdapterView<?> parent) {}
        });

        toggleThemeBtn.setOnClickListener(v -> {
            int current = AppCompatDelegate.getDefaultNightMode();
            if (current == AppCompatDelegate.MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
        });
    }

    private void loadTransactions() {
        listData.clear();
        totalAmount = 0.0;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM transactions", null);
        while (cursor.moveToNext()) {
            double amount = cursor.getDouble(cursor.getColumnIndex("amount"));
            String category = cursor.getString(cursor.getColumnIndex("category"));
            String type = cursor.getString(cursor.getColumnIndex("type"));
            totalAmount += amount;
            listData.add(type + ": " + category + " - " + convertCurrency(amount));
        }
        cursor.close();
        listAdapter.notifyDataSetChanged();
        totalText.setText("Total: " + convertCurrency(totalAmount));
    }

    private String convertCurrency(double amount) {
        switch (currentCurrency) {
            case "USD": return "$" + String.format("%.2f", amount * 0.012);
            case "EUR": return "€" + String.format("%.2f", amount * 0.011);
            default: return "₹" + String.format("%.2f", amount);
        }
    }
}