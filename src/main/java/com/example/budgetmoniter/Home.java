package com.example.budgetmoniter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class Home extends AppCompatActivity {

    private Button newTransactionButton, resetButton;
    private ListView transactionsList;
    private TextView currentBalanceNum, totalIncomeNum, totalExpenseNum;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        dbHelper = new DBHelper(this);

        newTransactionButton = findViewById(R.id.newtransaction);
        resetButton = findViewById(R.id.reset);
        transactionsList = findViewById(R.id.transactions_list);
        currentBalanceNum = findViewById(R.id.current_balance_num);
        totalIncomeNum = findViewById(R.id.total_income_num);
        totalExpenseNum = findViewById(R.id.total_expense_num);

        newTransactionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, AddTransactionActivity.class));
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper.resetData();
                updateUI();
                setupTransactionsList();
            }
        });

        setupTransactionsList();
        updateUI();
    }

    private void updateUI() {
        double currentBalance = dbHelper.calculateCurrentBalance();
        double totalIncome = dbHelper.calculateTotalAmount("Income");
        double totalExpense = dbHelper.calculateTotalAmount("Expense");

        currentBalanceNum.setText(String.valueOf(currentBalance));
        totalIncomeNum.setText(String.valueOf(totalIncome));
        totalExpenseNum.setText(String.valueOf(totalExpense));
    }

    private void setupTransactionsList() {
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this,
                R.layout.transaction_item,
                dbHelper.getTransactions(),
                new String[]{"description", "amount", "type"},
                new int[]{R.id.transaction_description, R.id.transaction_amount, R.id.transaction_type},
                0
        );

        transactionsList.setAdapter(adapter);
    }
}
